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
import {
  DayOfWeekDto,
  DoctorSchedule,
  IntervalDTO,
  IntervalListDTO,
  ScheduleService,
} from '../../services/schedule.service';
import { HttpClientModule } from '@angular/common/http';

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
    ScheduleService,
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

  readonly intervals = [5, 10, 15, 20, 30, 45, 60];
  readonly hours = Array.from({ length: 24 }, (_, i) => i);

  constructor(
    private fb: FormBuilder,
    private scheduleService: ScheduleService,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.scheduleForm = this.fb.group({
      consultationIntervalMinutes: ['', Validators.required],
      startHour: ['', Validators.required],
      startMinute: [0, Validators.required],
      endHour: ['', Validators.required],
      endMinute: [0, Validators.required],
      weeksRepeat: [4, [Validators.required, Validators.min(1), Validators.max(52)]],
      year: [new Date().getFullYear(), Validators.required]
    });
  }

  ngOnInit(): void {
    this.scheduleForm.patchValue({
      startHour: 8,
      endHour: 17,
      consultationIntervalMinutes: 30
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

  /**
   * Construye IntervalDTO[] (startTime/endTime) según intervalo de consulta
   */
  buildIntervals(
    startHour: number,
    startMinute: number,
    endHour: number,
    endMinute: number,
    consultationIntervalMinutes: number
  ): IntervalDTO[] {
    const intervals: IntervalDTO[] = [];

    let currentTotalMinutes = (startHour * 60) + startMinute;
    const endTotalMinutes = (endHour * 60) + endMinute;

    while (currentTotalMinutes < endTotalMinutes) {
      const nextTotalMinutes = currentTotalMinutes + consultationIntervalMinutes;
      if (nextTotalMinutes > endTotalMinutes) {
        break;
      }

      const fromHour = Math.floor(currentTotalMinutes / 60);
      const fromMinute = currentTotalMinutes % 60;
      const toHour = Math.floor(nextTotalMinutes / 60);
      const toMinute = nextTotalMinutes % 60;

      intervals.push({
        startTime: this.toTimeString(fromHour, fromMinute),
        endTime: this.toTimeString(toHour, toMinute),
      });

      currentTotalMinutes = nextTotalMinutes;
    }

    return intervals;
  }

  /**
   * Guardar la configuración
   */
  saveSchedule(): void {
    if (this.scheduleForm.invalid || !this.isDaySelectionValid()) {
      this.snackBar.open('Por favor completa todos los campos', 'Cerrar', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
      return;
    }

    if (!this.validateTimeRange()) {
      this.snackBar.open('La hora de cierre debe ser posterior a la hora de inicio', 'Cerrar', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
      return;
    }

    this.isLoading = true;
    this.cdr.markForCheck();
    const formValue = this.scheduleForm.value;
    const selectedDays = this.getSelectedDays();

    // Obtener el ID del doctor del localStorage o sesión
    const doctorId = localStorage.getItem('doctorId') || 'temp-id';

    const intervalConfig: IntervalListDTO = {
      intervals: this.buildIntervals(
        formValue.startHour,
        formValue.startMinute,
        formValue.endHour,
        formValue.endMinute,
        formValue.consultationIntervalMinutes
      ),
    };

    const scheduleData: DoctorSchedule = {
      days: this.mapDaysToDto(selectedDays),
      schedules: selectedDays.map(() => ({
        intervals: intervalConfig.intervals.map((interval) => ({ ...interval }))
      })),
      weeksRepeat: formValue.weeksRepeat,
      year: formValue.year,
    };

    this.scheduleService.saveDoctorSchedule(doctorId, scheduleData)
      .pipe(
        observeOn(asyncScheduler),
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
      next: () => {
        this.snackBar.open('¡Horario configurado exitosamente!', 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        // Limpiar localStorage
        localStorage.removeItem('doctorId');
        this.router.navigate(['/admin']);
      },
      error: (error) => {
        console.error('Error al guardar el horario:', error);
        this.snackBar.open('Error al guardar la configuración. Intenta de nuevo.', 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      }
    });
  }

  /**
   * Cancelar y volver
   */
  cancel(): void {
    localStorage.removeItem('doctorId');
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
}
