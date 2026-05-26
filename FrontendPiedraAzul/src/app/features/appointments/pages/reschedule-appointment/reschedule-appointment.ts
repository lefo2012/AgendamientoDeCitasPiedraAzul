import {
  ChangeDetectorRef,
  Component,
  Inject,
  OnDestroy,
  OnInit,
  PLATFORM_ID,
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { finalize } from 'rxjs';
import { DoctorDto } from '../../models/DoctorDto';
import { IntervalDto } from '../../models/IntervalDto';
import { IntervalListDto } from '../../models/IntervalListDto';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../services/doctor.service';
import { AuthService } from '../../../users/services/auth.service';
import { ReserveAppointmentDto } from '../../models/ReserveAppointmentDto';
import { ConfirmAppointmentDialog } from '../../../../shared/dialogs/confirm-appointment-dialog/confirm-appointment-dialog';
import { SuccessAppointmentDialog } from '../../../../shared/dialogs/success-appointment-dialog/success-appointment-dialog';
import { CurrentPatient } from '../../../users/models/CurrentPatient';
import { AppointmentSlotDto } from '../../models/AppointmentSlotDto';

@Component({
  selector: 'app-reschedule-appointment',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatSnackBarModule,
    MatNativeDateModule,
    MatProgressSpinnerModule,
    MatDialogModule,
  ],
  templateUrl: './reschedule-appointment.html',
  styleUrl: './reschedule-appointment.scss',
})
export class RescheduleAppointment implements OnInit, OnDestroy {
  readonly appointmentForm;

  appointmentId!: number;
  patientId: number | null = null;
  doctorId: number | null = null;
  specialty: string | null = null;
  doctors: DoctorDto[] = [];
  slots: AppointmentSlotDto[] = [];
  slotsByDate: Record<string, AppointmentSlotDto[]> = {};
  slotsForSelectedDate: AppointmentSlotDto[] = [];
  availableDateKeys = new Set<string>();

  isLoadingDoctors = false;
  isRescheduling = false;
  minDate = new Date();

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly fb: FormBuilder,
    private readonly doctorService: DoctorService,
    private readonly appointmentService: AppointmentService,
    private readonly authService: AuthService,
    private readonly snackBar: MatSnackBar,
    private readonly dialog: MatDialog,
    private readonly cdr: ChangeDetectorRef,
  ) {
    this.appointmentForm = this.fb.group({
      doctorId: [null as number | null, [Validators.required]],
      appointmentDate: [{ value: null as Date | null, disabled: true }, [Validators.required]],
      slot: [{ value: null as AppointmentSlotDto | null, disabled: true }, [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.patientId = this.resolveCurrentPatientId();
    this.appointmentService.getPendingAppointments(this.patientId ?? 0).subscribe({
      next: (response) => {
        const list = Array.isArray(response)
          ? response
          : ((response as any)?.pendingAppointments ?? []);
        this.appointmentId = list.length > 0 ? list[0].id : 0;
        this.doctorId = list.length > 0 ? list[0].doctor.id : null;
        this.cdr.detectChanges();
        if (isPlatformBrowser(this.platformId)) {
          this.loadDoctors();
        }
      },
      error: (err) => {
        this.cdr.detectChanges();
      },
    });
  }

  ngOnDestroy(): void {}

  get doctorIdControl() {
    return this.appointmentForm.controls.doctorId;
  }
  get appointmentDateControl() {
    return this.appointmentForm.controls.appointmentDate;
  }
  get slotControl() {
    return this.appointmentForm.controls.slot;
  }

  readonly appointmentDateFilter = (date: Date | null): boolean => {
    if (!date) return false;
    return this.availableDateKeys.has(this.toDateKey(date));
  };

  loadDoctors(): void {
    this.isLoadingDoctors = true;
    this.doctorIdControl.disable({ emitEvent: false });

    this.doctorService.getDoctorById(this.doctorId ?? 0).subscribe({
      next: (doctor) => {
        this.specialty = doctor.specialties[0] ?? '';
        this.doctorService
          .getDoctorsBySpeciality(this.specialty ?? '')
          .pipe(finalize(() => (this.isLoadingDoctors = false)))
          .subscribe({
            next: (doctors) => {
              this.doctors = doctors ?? [];
              if (this.doctors.length > 0) {
                this.doctorIdControl.enable({ emitEvent: false });
              } else {
                this.openSnackBar('No hay doctores disponibles.', 'info');
              }
            },
            error: () => this.openSnackBar('No se pudieron cargar los doctores.', 'error'),
          });
      },
    });
  }

  onDoctorChange(): void {
    this.appointmentDateControl.setValue(null, { emitEvent: false });
    this.slotControl.setValue(null);
    this.slotsForSelectedDate = [];

    const selectedDoctor = this.getSelectedDoctor();
    this.slots = selectedDoctor ? this.buildSlotsFromDoctor(selectedDoctor) : [];
    this.slotsByDate = this.groupSlotsByDate(this.slots);
    this.availableDateKeys = new Set(Object.keys(this.slotsByDate));

    if (selectedDoctor && this.slots.length > 0) {
      this.appointmentDateControl.enable({ emitEvent: false });
      this.slotControl.disable({ emitEvent: false });
    } else {
      this.appointmentDateControl.disable({ emitEvent: false });
      this.slotControl.disable({ emitEvent: false });
    }
  }

  onDateChange(): void {
    this.slotControl.setValue(null, { emitEvent: false });
    this.slotsForSelectedDate = [];

    const selectedDate = this.appointmentDateControl.value;
    if (!selectedDate) {
      this.slotControl.disable({ emitEvent: false });
      return;
    }

    const key = this.toDateKey(selectedDate);
    this.slotsForSelectedDate = this.slotsByDate[key] ?? [];

    if (this.slotsForSelectedDate.length > 0) {
      this.slotControl.enable({ emitEvent: false });
    } else {
      this.slotControl.disable({ emitEvent: false });
    }
  }

  rescheduleAppointment(): void {
    if (this.appointmentForm.invalid) {
      this.appointmentForm.markAllAsTouched();
      return;
    }

    const selectedDoctor = this.getSelectedDoctor();
    const selectedSlot = this.slotControl.value;
    if (!selectedDoctor || !selectedSlot) return;

    const dialogRef = this.dialog.open(ConfirmAppointmentDialog, {
      width: '400px',
      data: {
        doctorName: `${selectedDoctor.firstName} ${selectedDoctor.lastName}`,
        date: this.toDateKey(this.appointmentDateControl.value),
        time: selectedSlot.label,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.submitReschedule(selectedDoctor, selectedSlot);
      }
    });
  }

  private submitReschedule(selectedDoctor: DoctorDto, selectedSlot: AppointmentSlotDto): void {
    if (!this.patientId) {
      this.openSnackBar('No se pudo identificar el paciente.', 'error');
      return;
    }

    this.isRescheduling = true;

    const payload: ReserveAppointmentDto = {
      id: this.appointmentId,
      idPatient: this.patientId,
      idDoctor: selectedDoctor.id,
      interval: selectedSlot.interval,
      appointmentDate: this.toDateKey(this.appointmentDateControl.value),
    };

    this.appointmentService
      .rescheduleAppointment(payload)
      .pipe(finalize(() => (this.isRescheduling = false)))
      .subscribe({
        next: () => {
          const successRef = this.dialog.open(SuccessAppointmentDialog, {
            width: '450px',
            disableClose: true,
            data: {
              doctorName: `${selectedDoctor.firstName} ${selectedDoctor.lastName}`,
              date: this.toDateKey(this.appointmentDateControl.value),
              time: selectedSlot.label,
            },
          });

          successRef.afterClosed().subscribe(() => {
            this.router.navigate(['/']);
          });
        },
        error: () => this.openSnackBar('No fue posible reagendar la cita.', 'error'),
      });
  }

  canReschedule(): boolean {
    return this.appointmentForm.valid && !this.isRescheduling;
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  private getSelectedDoctor(): DoctorDto | undefined {
    const id = this.doctorIdControl.value;
    return id ? this.doctors.find((d) => d.id === id) : undefined;
  }

  private resolveCurrentPatientId(): number | null {
    const patient = this.authService.currentPatient();
    if (!patient) return null;

    for (const candidate of [patient.id, patient['idPatient'], patient['patientId']]) {
      const parsed = Number(candidate);
      if (Number.isFinite(parsed) && parsed > 0) return parsed;
    }
    return null;
  }

  private buildSlotsFromDoctor(doctor: DoctorDto): AppointmentSlotDto[] {
    const availableTimes = this.normalizeAvailableTimes(doctor.schedule?.availableTimes);
    const intervalMinutes = this.resolveAppointmentIntervalMinutes(doctor.appointmentInterval);
    const slots: AppointmentSlotDto[] = [];

    for (const dateKey of Object.keys(availableTimes).sort()) {
      const intervals = availableTimes[dateKey]?.intervals ?? [];
      for (const interval of intervals) {
        for (const split of this.splitInterval(interval, intervalMinutes)) {
          slots.push({
            appointmentDate: dateKey,
            interval: split,
            label: `${split.startTime} - ${split.endTime}`,
          });
        }
      }
    }
    return slots;
  }

  private normalizeAvailableTimes(
    availableTimes: Record<string, IntervalListDto> | Map<string, IntervalListDto> | undefined,
  ): Record<string, IntervalListDto> {
    if (!availableTimes) return {};
    if (availableTimes instanceof Map) return Object.fromEntries(availableTimes.entries());
    return availableTimes;
  }

  private resolveAppointmentIntervalMinutes(interval: IntervalDto | null): number {
    if (!interval) return 30;
    const duration = this.timeToMinutes(interval.endTime) - this.timeToMinutes(interval.startTime);
    return duration > 0 ? duration : 30;
  }

  private splitInterval(container: IntervalDto, step: number): IntervalDto[] {
    const start = this.timeToMinutes(container.startTime);
    const end = this.timeToMinutes(container.endTime);
    if (isNaN(start) || isNaN(end) || end <= start || step <= 0) return [];
    const chunks: IntervalDto[] = [];
    for (let p = start; p + step <= end; p += step) {
      chunks.push({ startTime: this.minutesToTime(p), endTime: this.minutesToTime(p + step) });
    }
    return chunks.length
      ? chunks
      : [{ startTime: this.minutesToTime(start), endTime: this.minutesToTime(end) }];
  }

  private timeToMinutes(t: string): number {
    const [h, m] = t.split(':').map(Number);
    return isNaN(h) || isNaN(m) ? NaN : h * 60 + m;
  }

  private minutesToTime(total: number): string {
    return `${String(Math.floor(total / 60)).padStart(2, '0')}:${String(total % 60).padStart(2, '0')}`;
  }

  private toDateKey(date: Date | string | null): string {
    if (!date) return '';
    if (typeof date === 'string') return date;
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  }

  private groupSlotsByDate(slots: AppointmentSlotDto[]): Record<string, AppointmentSlotDto[]> {
    return slots.reduce<Record<string, AppointmentSlotDto[]>>((acc, slot) => {
      (acc[slot.appointmentDate] ??= []).push(slot);
      return acc;
    }, {});
  }

  private openSnackBar(message: string, type: 'success' | 'error' | 'info'): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 4500,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['app-snackbar', `app-snackbar-${type}`],
    });
  }
}
