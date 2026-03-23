import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { AppointmentSlotDto } from '../../models/AppointmentSlotDto';
import { DoctorDto } from '../../models/DoctorDto';
import { IntervalDto } from '../../models/IntervalDto';
import { IntervalListDto } from '../../models/IntervalListDto';
import { PatientDto } from '../../models/PatientDto';
import { ScheduleService } from '../../services/schedule.service';

@Component({
  selector: 'app-create-appointment',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './create-appointment.html',
  styleUrl: './create-appointment.scss'
})
export class CreateAppointment implements OnInit {
  readonly appointmentForm;

  doctors: DoctorDto[] = [];
  slots: AppointmentSlotDto[] = [];
  patient: PatientDto | null = null;
  patientError = '';

  isLoadingDoctors = false;
  isSearchingPatient = false;
  isReserving = false;

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly router: Router,
    private readonly fb: FormBuilder,
    private readonly scheduleService: ScheduleService,
    private readonly snackBar: MatSnackBar
  ) {
    this.appointmentForm = this.fb.group({
      identificationNumber: ['', [Validators.required]],
      doctorId: [null as number | null, [Validators.required]],
      slot: [{ value: null as AppointmentSlotDto | null, disabled: true }, [Validators.required]],
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctors();
    }
  }

  get identificationNumberControl() {
    return this.appointmentForm.controls.identificationNumber;
  }

  get doctorIdControl() {
    return this.appointmentForm.controls.doctorId;
  }

  get slotControl() {
    return this.appointmentForm.controls.slot;
  }

  loadDoctors(): void {
    this.isLoadingDoctors = true;
    this.doctorIdControl.disable({ emitEvent: false });
    this.slotControl.disable({ emitEvent: false });
    this.slotControl.setValue(null, { emitEvent: false });

    this.scheduleService
      .getAllDoctors()
      .pipe(finalize(() => (this.isLoadingDoctors = false)))
      .subscribe({
        next: (doctors: DoctorDto[]) => {
          this.doctors = doctors ?? [];

          if (this.doctors.length > 0) {
            this.doctorIdControl.enable({ emitEvent: false });
          } else {
            this.snackBar.open('No hay doctores disponibles para agendar.', 'Cerrar', {
              duration: 3000,
            });
          }
        },
        error: () => {
          this.snackBar.open('No se pudieron cargar los doctores.', 'Cerrar', {
            duration: 3000,
          });
        },
      });
  }

  searchPatient(): void {
    const identificationNumber = this.identificationNumberControl.value?.trim() ?? '';
    if (!identificationNumber) {
      this.identificationNumberControl.markAsTouched();
      return;
    }

    this.patient = null;
    this.patientError = '';
    this.isSearchingPatient = true;

    this.scheduleService
      .getPatientByIdentificationNumber(identificationNumber)
      .pipe(finalize(() => (this.isSearchingPatient = false)))
      .subscribe({
        next: (patient: PatientDto) => {
          this.patient = patient;
          this.snackBar.open('Paciente encontrado.', 'Cerrar', {
            duration: 2500,
          });
        },
        error: (error: { status?: number }) => {
          this.patient = null;
          this.patientError =
            error?.status === 404
              ? 'No se encontro paciente con ese numero de identificacion.'
              : 'No se pudo consultar el paciente.';
          this.snackBar.open(this.patientError, 'Cerrar', {
            duration: 3500,
          });
        },
      });
  }

  onDoctorChange(): void {
    this.slotControl.setValue(null);
    const selectedDoctor = this.getSelectedDoctor();
    this.slots = selectedDoctor ? this.buildSlotsFromDoctor(selectedDoctor) : [];

    if (selectedDoctor && this.slots.length > 0) {
      this.slotControl.enable({ emitEvent: false });
    } else {
      this.slotControl.disable({ emitEvent: false });
    }
  }

  reserveAppointment(): void {
    if (this.appointmentForm.invalid || !this.patient) {
      this.appointmentForm.markAllAsTouched();
      return;
    }

    const selectedDoctor = this.getSelectedDoctor();
    const selectedSlot = this.slotControl.value;

    if (!selectedDoctor || !selectedSlot) {
      return;
    }

    this.isReserving = true;
    this.scheduleService
      .reserveAppointment({
        idPatient: this.patient.id,
        idDoctor: selectedDoctor.id,
        interval: selectedSlot.interval,
        appointmentDate: selectedSlot.appointmentDate,
      })
      .pipe(finalize(() => (this.isReserving = false)))
      .subscribe({
        next: () => {
          this.snackBar.open('Cita agendada correctamente.', 'Cerrar', {
            duration: 3000,
          });
          this.resetAndReloadForm();
        },
        error: () => {
          this.snackBar.open('No fue posible agendar la cita.', 'Cerrar', {
            duration: 3500,
          });
        },
      });
  }

  canReserve(): boolean {
    return this.appointmentForm.valid && !!this.patient && !this.isReserving && !this.isSearchingPatient;
  }

  private getSelectedDoctor(): DoctorDto | undefined {
    const selectedDoctorId = this.doctorIdControl.value;
    if (!selectedDoctorId) {
      return undefined;
    }

    return this.doctors.find((doctor) => doctor.id === selectedDoctorId);
  }

  private buildSlotsFromDoctor(doctor: DoctorDto): AppointmentSlotDto[] {
    const availableTimes = this.normalizeAvailableTimes(doctor.schedule?.availableTimes);
    const dateKeys = Object.keys(availableTimes).sort();
    const intervalMinutes = this.resolveAppointmentIntervalMinutes(doctor.appointmentInterval);

    const generatedSlots: AppointmentSlotDto[] = [];

    for (const dateKey of dateKeys) {
      const intervalList = availableTimes[dateKey];
      const intervals = intervalList?.intervals ?? [];

      for (const interval of intervals) {
        const splitIntervals = this.splitInterval(interval, intervalMinutes);
        for (const splitInterval of splitIntervals) {
          generatedSlots.push({
            appointmentDate: dateKey,
            interval: splitInterval,
            label: `${this.formatDate(dateKey)} | ${splitInterval.startTime} - ${splitInterval.endTime}`,
          });
        }
      }
    }

    return generatedSlots;
  }

  private normalizeAvailableTimes(
    availableTimes: Record<string, IntervalListDto> | Map<string, IntervalListDto> | undefined
  ): Record<string, IntervalListDto> {
    if (!availableTimes) {
      return {};
    }

    if (availableTimes instanceof Map) {
      return Object.fromEntries(availableTimes.entries());
    }

    return availableTimes;
  }

  private resolveAppointmentIntervalMinutes(appointmentInterval: IntervalDto | null): number {
    if (!appointmentInterval) {
      return 30;
    }

    const start = this.timeToMinutes(appointmentInterval.startTime);
    const end = this.timeToMinutes(appointmentInterval.endTime);
    const duration = end - start;

    return duration > 0 ? duration : 30;
  }

  private splitInterval(containerInterval: IntervalDto, stepMinutes: number): IntervalDto[] {
    const start = this.timeToMinutes(containerInterval.startTime);
    const end = this.timeToMinutes(containerInterval.endTime);

    if (Number.isNaN(start) || Number.isNaN(end) || end <= start || stepMinutes <= 0) {
      return [];
    }

    const chunks: IntervalDto[] = [];
    let pointer = start;

    while (pointer + stepMinutes <= end) {
      chunks.push({
        startTime: this.minutesToTime(pointer),
        endTime: this.minutesToTime(pointer + stepMinutes),
      });
      pointer += stepMinutes;
    }

    if (chunks.length === 0) {
      chunks.push({
        startTime: this.minutesToTime(start),
        endTime: this.minutesToTime(end),
      });
    }

    return chunks;
  }

  private timeToMinutes(rawTime: string): number {
    const [hoursRaw, minutesRaw] = rawTime.split(':');
    const hours = Number(hoursRaw);
    const minutes = Number(minutesRaw);

    if (Number.isNaN(hours) || Number.isNaN(minutes)) {
      return Number.NaN;
    }

    return hours * 60 + minutes;
  }

  private minutesToTime(totalMinutes: number): string {
    const hours = Math.floor(totalMinutes / 60);
    const minutes = totalMinutes % 60;

    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
  }

  private formatDate(dateRaw: string): string {
    const parsedDate = new Date(`${dateRaw}T00:00:00`);
    if (Number.isNaN(parsedDate.getTime())) {
      return dateRaw;
    }

    return new Intl.DateTimeFormat('es-CO', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    }).format(parsedDate);
  }

  private resetAndReloadForm(): void {
    this.patient = null;
    this.patientError = '';
    this.slots = [];

    this.appointmentForm.reset({
      identificationNumber: '',
      doctorId: null,
      slot: null,
    });

    this.slotControl.disable({ emitEvent: false });
    this.loadDoctors();
  }

  goBack(): void {
    this.router.navigate(['/citas/agendar']);
  }
}
