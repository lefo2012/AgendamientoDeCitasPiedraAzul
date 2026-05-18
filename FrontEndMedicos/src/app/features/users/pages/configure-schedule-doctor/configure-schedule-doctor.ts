import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatStepperModule } from '@angular/material/stepper';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { asyncScheduler, finalize, observeOn } from 'rxjs';
import { DayOfWeekDto } from '../../../appointments/models/DayOfWeekDto';
import { DoctorSchedule } from '../../../appointments/models/DoctorSchedule';
import { IntervalDto } from '../../../appointments/models/IntervalDto';
import { IntervalListDto } from '../../../appointments/models/IntervalListDto';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

interface DaySelection {
  value: number;
  label: string;
  selected: boolean;
}

@Component({
  selector: 'app-configure-schedule-doctor',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatStepperModule,
    MatDividerModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatSnackBarModule,
    HttpClientModule
  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'es-CO' }
  ],
  templateUrl: './configure-schedule-doctor.html',
  styleUrl: './configure-schedule-doctor.scss',
})
export class ConfigureScheduleDoctor implements OnInit {
  scheduleForm: FormGroup;
  isLoading = false;

  daysOfWeek: DaySelection[] = [
    { value: 1, label: 'Lunes', selected: false },
    { value: 2, label: 'Martes', selected: false },
    { value: 3, label: 'Miércoles', selected: false },
    { value: 4, label: 'Jueves', selected: false },
    { value: 5, label: 'Viernes', selected: false },
    { value: 6, label: 'Sábado', selected: false },
    { value: 0, label: 'Domingo', selected: false }
  ];

  readonly hours = Array.from({ length: 24 }, (_, i) => i);

  constructor(
    private fb: FormBuilder,
    private doctorService: DoctorService,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.scheduleForm = this.fb.group({
      startHour: ['', Validators.required],
      startMinute: [0, Validators.required],
      endHour: ['', Validators.required],
      endMinute: [0, Validators.required],
      hasSecondShift: [false],
      secondStartHour: [null],
      secondStartMinute: [0],
      secondEndHour: [null],
      secondEndMinute: [0],
      weeksRepeat: [4, [Validators.required, Validators.min(1), Validators.max(52)]],
      year: [new Date().getFullYear(), Validators.required]
    });
  }

  ngOnInit(): void {
    this.scheduleForm.patchValue({
      startHour: 7,
      endHour: 18
    });
  }

  /**
   * Obtener los días seleccionados
   */
  getSelectedDays(): number[] {
    return this.daysOfWeek.filter(day => day.selected).map(day => day.value);
  }

  /**
   * Validar que al menos un día esté seleccionado
   */
  isDaySelectionValid(): boolean {
    return this.getSelectedDays().length > 0;
  }

  /**
   * Formatear número con padding
   */
  padNumber(num: number): string {
    return String(num).padStart(2, '0');
  }

  /**
   * Convierte los días seleccionados al formato DayOfWeek del backend
   */
  mapDaysToDto(days: number[]): DayOfWeekDto[] {
    const map: Record<number, DayOfWeekDto> = {
      0: 'SUNDAY',
      1: 'MONDAY',
      2: 'TUESDAY',
      3: 'WEDNESDAY',
      4: 'THURSDAY',
      5: 'FRIDAY',
      6: 'SATURDAY',
    };

    return days.map((day) => map[day]);
  }

  /**
   * Convierte un valor hora/minuto a HH:mm
   */
  toTimeString(hour: number, minute: number): string {
    return `${this.padNumber(hour)}:${this.padNumber(minute)}`;
  }

  buildIntervals(formValue: any): IntervalDto[] {
    const intervals: IntervalDto[] = [
      {
        startTime: this.toTimeString(formValue.startHour, formValue.startMinute),
        endTime: this.toTimeString(formValue.endHour, formValue.endMinute),
      }
    ];

    if (formValue.hasSecondShift) {
      intervals.push({
        startTime: this.toTimeString(formValue.secondStartHour, formValue.secondStartMinute),
        endTime: this.toTimeString(formValue.secondEndHour, formValue.secondEndMinute),
      });
    }

    return intervals;
  }

  private resolveDoctorId(): string | null {
    const currentDoctorId = this.authService.currentPatient()?.id;

    if (currentDoctorId !== undefined && currentDoctorId !== null) {
      return String(currentDoctorId);
    }

    const storedDoctorId = localStorage.getItem('doctorId');

    if (storedDoctorId && storedDoctorId.trim().length > 0) {
      return storedDoctorId;
    }

    return null;
  }

  /**
   * Guardar la configuración
   */
  saveSchedule(): void {
    if (this.scheduleForm.invalid || !this.isDaySelectionValid()) {
      this.openSnackBar('Por favor completa todos los campos', 'error');
      return;
    }

    if (!this.validateTimeRange()) {
      this.openSnackBar('La hora de cierre debe ser posterior a la hora de inicio', 'error');
      return;
    }

    if (!this.validateSecondTimeRange()) {
      this.openSnackBar('Verifica la segunda franja horaria: debe ser válida y posterior a la primera', 'error');
      return;
    }

    this.isLoading = true;
    this.cdr.markForCheck();
    const formValue = this.scheduleForm.value;
    const selectedDays = this.getSelectedDays();
    const doctorId = this.resolveDoctorId();

    if (!doctorId) {
      this.isLoading = false;
      this.cdr.markForCheck();
      this.openSnackBar('No se pudo identificar el medico para configurar el horario.', 'error');
      return;
    }

    const intervalConfig: IntervalListDto = {
      intervals: this.buildIntervals(formValue),
    };

    const scheduleData: DoctorSchedule = {
      days: this.mapDaysToDto(selectedDays),
      schedules: selectedDays.map(() => ({
        intervals: intervalConfig.intervals.map((interval) => ({ ...interval }))
      })),
      weeksRepeat: formValue.weeksRepeat,
      year: formValue.year,
    };

    this.doctorService.saveDoctorSchedule(doctorId, scheduleData)
      .pipe(
        observeOn(asyncScheduler),
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
      next: () => {
        this.openSnackBar('¡Horario configurado exitosamente!', 'success');
        this.router.navigate(['/admin']);
      },
      error: (error) => {
        console.error('Error al guardar el horario:', error);
        this.openSnackBar('Error al guardar la configuración. Intenta de nuevo.', 'error');
      }
    });
  }

  /**
   * Cancelar y volver
   */
  cancel(): void {
    this.router.navigate(['/admin']);
  }

  /**
   * Validar que endHour sea posterior a startHour
   */
  validateTimeRange(): boolean {
    const startHour = this.scheduleForm.get('startHour')?.value;
    const endHour = this.scheduleForm.get('endHour')?.value;
    const startMinute = this.scheduleForm.get('startMinute')?.value;
    const endMinute = this.scheduleForm.get('endMinute')?.value;
    
    if (startHour !== null && endHour !== null) {
      if (startHour < endHour) return true;
      if (startHour === endHour && startMinute < endMinute) return true;
    }
    return false;
  }

  validateSecondTimeRange(): boolean {
    const hasSecondShift = this.scheduleForm.get('hasSecondShift')?.value;
    if (!hasSecondShift) {
      return true;
    }

    const secondStartHour = this.scheduleForm.get('secondStartHour')?.value;
    const secondStartMinute = this.scheduleForm.get('secondStartMinute')?.value;
    const secondEndHour = this.scheduleForm.get('secondEndHour')?.value;
    const secondEndMinute = this.scheduleForm.get('secondEndMinute')?.value;

    if (secondStartHour === null || secondStartHour === undefined || secondEndHour === null || secondEndHour === undefined) {
      return false;
    }

    const secondStartTotalMinutes = (secondStartHour * 60) + secondStartMinute;
    const secondEndTotalMinutes = (secondEndHour * 60) + secondEndMinute;

    if (secondStartTotalMinutes >= secondEndTotalMinutes) {
      return false;
    }

    const firstEndHour = this.scheduleForm.get('endHour')?.value;
    const firstEndMinute = this.scheduleForm.get('endMinute')?.value;
    const firstEndTotalMinutes = (firstEndHour * 60) + firstEndMinute;

    return secondStartTotalMinutes > firstEndTotalMinutes;
  }

  private openSnackBar(message: string, type: 'success' | 'error' | 'info'): void {
    const panelClass = ['app-snackbar'];
    if (type === 'success') {
      panelClass.push('app-snackbar-success');
    } else if (type === 'error') {
      panelClass.push('app-snackbar-error');
    } else {
      panelClass.push('app-snackbar-info');
    }

    this.snackBar.open(message, 'Cerrar', {
      duration: 4500,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass,
    });
  }
}
