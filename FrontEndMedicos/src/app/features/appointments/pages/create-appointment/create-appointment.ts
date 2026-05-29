import { ChangeDetectorRef, Component, Inject, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { finalize, Subscription } from 'rxjs';
import { AppointmentSlotDto } from '../../models/AppointmentSlotDto';
import { DoctorDto } from '../../models/DoctorDto';
import { IntervalDto } from '../../models/IntervalDto';
import { IntervalListDto } from '../../models/IntervalListDto';
import { PatientDto } from '../../models/PatientDto';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../services/doctor.service';
import { PatientService } from '../../services/patient.service';
import { ConfirmAppointmentDialog } from '../../../../shared/dialogs/confirm-appointment-dialog/confirm-appointment-dialog';
import { SuccessAppointmentDialog } from '../../../../shared/dialogs/success-appointment-dialog/success-appointment-dialog';

@Component({
  selector: 'app-create-appointment',
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
    MatAutocompleteModule,
    MatProgressSpinnerModule,
    MatDialogModule
  ],
  templateUrl: './create-appointment.html',
  styleUrl: './create-appointment.scss'
})
export class CreateAppointment implements OnInit, OnDestroy {
  readonly appointmentForm;

  doctors: DoctorDto[] = [];
  slots: AppointmentSlotDto[] = [];
  slotsByDate: Record<string, AppointmentSlotDto[]> = {};
  slotsForSelectedDate: AppointmentSlotDto[] = [];
  availableDateKeys = new Set<string>();
  patient: PatientDto | null = null;
  patientError = '';
  
  // Autocomplete properties
  filteredPatients: PatientDto[] = [];
  isLoadingPatients = false;
  private patientSearchDebounceTimer: ReturnType<typeof setTimeout> | null = null;
  private patientSearchSubscription: Subscription | null = null;

  isLoadingDoctors = false;
  isSearchingPatient = false;
  isReserving = false;
  minDate = new Date();

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly router: Router,
    private readonly fb: FormBuilder,
    private readonly doctorService: DoctorService,
    private readonly patientService: PatientService,
    private readonly appointmentService: AppointmentService,
    private readonly snackBar: MatSnackBar,
    private readonly cdr: ChangeDetectorRef,
    private readonly dialog: MatDialog
  ) {
    this.appointmentForm = this.fb.group({
      identificationNumber: ['', [Validators.required]],
      doctorId: [null as number | null, [Validators.required]],
      appointmentDate: [{ value: null as Date | null, disabled: true }, [Validators.required]],
      slot: [{ value: null as AppointmentSlotDto | null, disabled: true }, [Validators.required]],
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctors();
    }
  }

  ngOnDestroy(): void {
    if (this.patientSearchDebounceTimer) {
      clearTimeout(this.patientSearchDebounceTimer);
      this.patientSearchDebounceTimer = null;
    }

    this.patientSearchSubscription?.unsubscribe();
  }

  onIdentificationInput(event: Event): void {
    const identificationNumber = (event.target as HTMLInputElement).value.trim();

    if (this.patient && this.patient.identificationNumber !== identificationNumber) {
      this.patient = null;
    }

    if (this.patientSearchDebounceTimer) {
      clearTimeout(this.patientSearchDebounceTimer);
      this.patientSearchDebounceTimer = null;
    }

    this.patientSearchSubscription?.unsubscribe();

    if (!identificationNumber || identificationNumber.length < 3) {
      this.isLoadingPatients = false;
      this.filteredPatients = [];
      this.patientError = '';
      this.cdr.detectChanges();
      return;
    }

    this.patientSearchDebounceTimer = setTimeout(() => {
      this.runPatientSearch(identificationNumber);
    }, 250);
  }

  private runPatientSearch(identificationNumber: string): void {
    this.isLoadingPatients = true;
    this.patientError = '';

    this.patientSearchSubscription = this.patientService
      .searchPatientsByIdentificationNumber(identificationNumber)
      .pipe(finalize(() => {
        this.isLoadingPatients = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: (patients: PatientDto[]) => {
          const currentTerm = (this.identificationNumberControl.value ?? '').toString().trim();
          if (currentTerm !== identificationNumber) {
            return;
          }

          this.filteredPatients = (patients ?? []).filter((patient) =>
            String(patient.identificationNumber ?? '').startsWith(currentTerm)
          );

          this.patientError = this.filteredPatients.length === 0
            ? 'No se encontraron pacientes con ese número de identificación.'
            : '';
        },
        error: (error: { status?: number }) => {
          const currentTerm = (this.identificationNumberControl.value ?? '').toString().trim();
          if (currentTerm !== identificationNumber) {
            return;
          }

          this.filteredPatients = [];
          this.patientError =
            error?.status === 400 || error?.status === 404
              ? 'No se encontraron pacientes con ese número de identificación.'
              : 'Error al buscar pacientes. Intenta nuevamente.';
        },
      });
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

  get appointmentDateControl() {
    return this.appointmentForm.controls.appointmentDate;
  }

  readonly appointmentDateFilter = (date: Date | null): boolean => {
    if (!date) {
      return false;
    }

    return this.availableDateKeys.has(this.toDateKey(date));
  };

  loadDoctors(): void {
    this.isLoadingDoctors = true;
    this.doctorIdControl.disable({ emitEvent: false });
    this.appointmentDateControl.disable({ emitEvent: false });
    this.appointmentDateControl.setValue(null, { emitEvent: false });
    this.slotControl.disable({ emitEvent: false });
    this.slotControl.setValue(null, { emitEvent: false });
    this.slotsForSelectedDate = [];
    this.slotsByDate = {};
    this.availableDateKeys.clear();

    this.doctorService
      .getAllDoctors()
      .pipe(finalize(() => (this.isLoadingDoctors = false)))
      .subscribe({
        next: (doctors: DoctorDto[]) => {
          this.doctors = doctors ?? [];

          if (this.doctors.length > 0) {
            this.doctorIdControl.enable({ emitEvent: false });
          } else {
            this.openSnackBar('No hay doctores disponibles para agendar.', 'info');
          }
        },
        error: () => {
          this.openSnackBar('No se pudieron cargar los doctores.', 'error');
        },
      });
  }

  onPatientSelected(selectedIdentification: string): void {
    const selectedPatient = this.filteredPatients.find(
      (patient) => patient.identificationNumber === selectedIdentification
    );

    if (!selectedPatient) {
      return;
    }

    this.patient = selectedPatient;
    this.identificationNumberControl.setValue(selectedPatient.identificationNumber, { emitEvent: false });
    this.filteredPatients = [];
    this.patientError = '';
    this.openSnackBar('Paciente seleccionado correctamente.', 'success');
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

    this.patientService
      .getPatientByIdentificationNumber(identificationNumber)
      .pipe(finalize(() => (this.isSearchingPatient = false)))
      .subscribe({
        next: (patient: PatientDto) => {
          this.patient = patient;
          this.openSnackBar('Paciente encontrado.', 'success');
        },
        error: (error: { status?: number }) => {
          this.patient = null;
          this.patientError =
            error?.status === 404
              ? 'No se encontro paciente con ese numero de identificacion.'
              : 'No se pudo consultar el paciente.';
          this.openSnackBar(this.patientError, 'error');
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

    const selectedKey = this.toDateKey(selectedDate);
    this.slotsForSelectedDate = this.slotsByDate[selectedKey] ?? [];

    if (this.slotsForSelectedDate.length > 0) {
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

    // Show confirmation dialog
    const dialogRef = this.dialog.open(ConfirmAppointmentDialog, {
      width: '400px',
      disableClose: false,
      data: {
        doctorName: `${selectedDoctor.firstName} ${selectedDoctor.lastName}`,
        patientName: `${this.patient.firstName} ${this.patient.lastName}`,
        date: this.toDateKey(this.appointmentDateControl.value),
        time: selectedSlot.label,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.submitAppointment(selectedDoctor, selectedSlot);
      }
    });
  }

  private submitAppointment(selectedDoctor: DoctorDto, selectedSlot: AppointmentSlotDto): void {
    this.isReserving = true;
    this.appointmentService
      .reserveAppointment({
        idPatient: this.patient!.id,
        idDoctor: selectedDoctor.id,
        interval: selectedSlot.interval,
        appointmentDate: this.toDateKey(this.appointmentDateControl.value),
      })
      .pipe(finalize(() => (this.isReserving = false)))
      .subscribe({
        next: () => {
          const dialogRef = this.dialog.open(SuccessAppointmentDialog, {
            width: '450px',
            disableClose: true,
            data: {
              doctorName: `${selectedDoctor.firstName} ${selectedDoctor.lastName}`,
              patientName: `${this.patient?.firstName} ${this.patient?.lastName}`,
              date: this.toDateKey(this.appointmentDateControl.value),
              time: selectedSlot.label,
            }
          });

          dialogRef.afterClosed().subscribe(() => {
            this.resetAndReloadForm();
          });
        },
        error: (error) => {
          const message = this.resolveReserveErrorMessage(error);
          this.openSnackBar(message, 'error');
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
            label: `${splitInterval.startTime} - ${splitInterval.endTime}`,
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

  private groupSlotsByDate(slots: AppointmentSlotDto[]): Record<string, AppointmentSlotDto[]> {
    return slots.reduce<Record<string, AppointmentSlotDto[]>>((accumulator, slot) => {
      const key = slot.appointmentDate;
      if (!accumulator[key]) {
        accumulator[key] = [];
      }
      accumulator[key].push(slot);
      return accumulator;
    }, {});
  }

  private toDateKey(date: Date | string | null): string {
    if (!date) {
      return '';
    }

    if (typeof date === 'string') {
      return date;
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private resetAndReloadForm(): void {
    this.patient = null;
    this.patientError = '';
    this.slots = [];
    this.slotsByDate = {};
    this.slotsForSelectedDate = [];
    this.availableDateKeys.clear();

    this.appointmentForm.reset({
      identificationNumber: '',
      doctorId: null,
      appointmentDate: null,
      slot: null,
    });

    this.appointmentDateControl.disable({ emitEvent: false });
    this.slotControl.disable({ emitEvent: false });
    this.loadDoctors();
  }

  goBack(): void {
    this.router.navigate(['/citas/agendar']);
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

  private resolveReserveErrorMessage(error: unknown): string {
    const defaultMessage = 'No fue posible agendar la cita.';
    const message = this.extractErrorMessage(error);

    if (!message) {
      return defaultMessage;
    }

    const normalized = message.toLowerCase();
    if (normalized.includes('límite de citas pendientes') || normalized.includes('limite de citas pendientes')) {
      return 'El paciente ya tiene una cita pendiente. Debe atenderla o cancelarla antes de agendar otra.';
    }

    return message;
  }

  private extractErrorMessage(error: unknown): string {
    if (!error || typeof error !== 'object') {
      return '';
    }

    const httpError = error as { error?: unknown; message?: string };
    if (typeof httpError.error === 'string') {
      const raw = httpError.error.trim();
      if (!raw) {
        return httpError.message ?? '';
      }

      try {
        const parsed = JSON.parse(raw) as { message?: string };
        return parsed.message ?? raw;
      } catch {
        return raw;
      }
    }

    if (httpError.error && typeof httpError.error === 'object') {
      const payload = httpError.error as { message?: string };
      return payload.message ?? httpError.message ?? '';
    }

    return httpError.message ?? '';
  }
}
