import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../services/auth.service';
import { PatientService } from '../../services/patient.service';
import { AppointmentService } from '../../../appointments/services/appointment.service';
import { PatientProfile } from '../../models/PatientProfile';
import { UpdatePatientRequest } from '../../models/UpdatePatientRequest';
import { PendingAppointment } from '../../../appointments/models/PendingAppointment';
import { GENDER_OPTIONS } from '../../models/GenderEnum';

@Component({
  selector: 'app-patient-account',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatProgressBarModule,
    MatSnackBarModule,
    MatDividerModule
  ],
  templateUrl: './patient-account.html',
  styleUrl: './patient-account.scss'
})
export class PatientAccount {
  readonly genderOptions = GENDER_OPTIONS;
  patientId: number | null = null;
  patientEmail = '';
  patientProfile: PatientProfile | null = null;
  pendingAppointments: PendingAppointment[] = [];
  isLoadingProfile = false;
  isSaving = false;
  isLoadingAppointments = false;
  profileError = '';
  appointmentError = '';
  formSubmitted = false;

  profileForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private patientService: PatientService,
    private appointmentService: AppointmentService,
    private snackBar: MatSnackBar
  ) {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$')]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$')]],
      documentType: ['', Validators.required],
      identificationNumber: ['', [Validators.required, Validators.pattern('^[0-9]{6,12}$')]],
      birthDate: [null as Date | null, Validators.required],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      gender: ['', Validators.required],
      active: [true, Validators.required]
    });
    this.patientId = this.resolveCurrentPatientId();
    this.loadProfile();
    this.loadPendingAppointments();
  }

  loadProfile(): void {
    this.profileError = '';

    if (!this.patientId) {
      this.profileError = 'No se pudo identificar el paciente activo.';
      return;
    }

    this.isLoadingProfile = true;
    this.patientService.getPatientById(this.patientId).subscribe({
      next: (profile) => {
        this.patientProfile = profile;
        this.patientEmail = this.resolvePatientEmail(profile);
        this.patchProfileForm(profile);
        this.isLoadingProfile = false;
      },
      error: () => {
        this.profileError = 'No se pudo cargar la informacion del paciente.';
        this.isLoadingProfile = false;
      }
    });
  }

  loadPendingAppointments(): void {
    this.appointmentError = '';

    if (!this.patientId) {
      this.appointmentError = 'No se pudo identificar el paciente activo.';
      return;
    }

    this.isLoadingAppointments = true;
    this.appointmentService.getPendingAppointments(this.patientId).subscribe({
      next: (response) => {
        this.pendingAppointments = this.normalizePendingAppointments(response);
        this.isLoadingAppointments = false;
      },
      error: () => {
        this.appointmentError = 'No se pudo cargar la cita pendiente.';
        this.isLoadingAppointments = false;
      }
    });
  }

  saveProfile(): void {
    this.formSubmitted = true;
    this.profileError = '';

    if (!this.patientId) {
      this.profileError = 'No se pudo identificar el paciente activo.';
      return;
    }

    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const formValue = this.profileForm.getRawValue();
    const birthDateValue = formValue.birthDate as unknown;
    const birthDate = birthDateValue instanceof Date
      ? birthDateValue.toISOString().split('T')[0]
      : `${birthDateValue ?? ''}`.trim();

    const payload: UpdatePatientRequest = {
      id: this.patientId,
      documentType: `${formValue.documentType}`,
      identificationNumber: `${formValue.identificationNumber}`,
      firstName: `${formValue.firstName}`,
      lastName: `${formValue.lastName}`,
      birthDate,
      phone: `${formValue.phone}`,
      active: !!formValue.active,
      gender: `${formValue.gender}`
    };

    this.isSaving = true;
    this.patientService.updatePatient(payload).subscribe({
      next: (updatedProfile) => {
        this.patientProfile = updatedProfile;
        this.patientEmail = this.resolvePatientEmail(updatedProfile);
        this.patchProfileForm(updatedProfile);
        this.profileForm.markAsPristine();
        this.formSubmitted = false;
        this.isSaving = false;
        this.snackBar.open('Datos actualizados correctamente.', 'Cerrar', { duration: 3000 });
      },
      error: () => {
        this.profileError = 'No se pudo actualizar la informacion del paciente.';
        this.isSaving = false;
      }
    });
  }

  fieldInvalid(fieldName: string): boolean {
    const control = this.profileForm.get(fieldName);
    return !!control && control.invalid && (control.touched || this.formSubmitted);
  }

  get pendingAppointment(): PendingAppointment | null {
    return this.pendingAppointments.length > 0 ? this.pendingAppointments[0] : null;
  }

  private patchProfileForm(profile: PatientProfile): void {
    this.profileForm.patchValue({
      firstName: profile.firstName ?? '',
      lastName: profile.lastName ?? '',
      documentType: profile.documentType ?? '',
      identificationNumber: profile.identificationNumber ?? '',
      birthDate: this.parseBirthDate(profile.birthDate),
      phone: profile.phone ?? '',
      gender: profile.gender ?? '',
      active: profile.active ?? true
    });
  }

  private parseBirthDate(rawDate: string | undefined): Date | null {
    if (!rawDate) {
      return null;
    }

    const parsed = new Date(rawDate);
    return Number.isNaN(parsed.getTime()) ? null : parsed;
  }

  private normalizePendingAppointments(
    response: PendingAppointment[] | { pendingAppointments?: PendingAppointment[] }
  ): PendingAppointment[] {
    if (Array.isArray(response)) {
      return response;
    }

    if (response?.pendingAppointments && Array.isArray(response.pendingAppointments)) {
      return response.pendingAppointments;
    }

    return [];
  }

  private resolvePatientEmail(profile: PatientProfile): string {
    const userEmail = profile.user?.email;
    const directEmail = (profile as { email?: string }).email;
    return userEmail ?? directEmail ?? '';
  }

  private resolveCurrentPatientId(): number | null {
    const patient = this.authService.currentPatient();

    if (!patient) {
      return null;
    }

    const candidateIds = [
      patient.id,
      patient['idPatient'],
      patient['patientId']
    ];

    for (const candidate of candidateIds) {
      const parsed = Number(candidate);
      if (Number.isFinite(parsed) && parsed > 0) {
        return parsed;
      }
    }

    return null;
  }
}
