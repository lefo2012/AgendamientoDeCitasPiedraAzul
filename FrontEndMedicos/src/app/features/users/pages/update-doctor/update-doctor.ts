import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatDatepickerModule } from '@angular/material/datepicker'; // ← agregar
import { MatNativeDateModule } from '@angular/material/core'; // ← agregar
import { finalize } from 'rxjs';
import { GENDER_OPTIONS } from '../../models/GenderEnum';
import { DocumentTypeEnum } from '../../models/DocumentTypeEnum';
import { SpecialtyEnum } from '../../models/SpecialtyEnum';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { UpdateDoctorDto } from '../../models/UpdateDoctorDto';

@Component({
  selector: 'app-update-doctor',
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
    MatSnackBarModule,
    MatSlideToggleModule,
    MatChipsModule,
    MatDividerModule,
    MatDatepickerModule, // ← agregar
    MatNativeDateModule, // ← agregar, sin esto el datepicker no renderiza
  ],
  templateUrl: './update-doctor.html',
  styleUrl: './update-doctor.scss',
})
export class UpdateDoctor implements OnInit {
  readonly form;

  doctorId!: number;
  isSaving = false;
  maxBirthDate = new Date(); // no permite fechas futuras

  genderOptions = Object.values(GENDER_OPTIONS);
  documentTypeOptions = Object.values(DocumentTypeEnum);
  specialtyOptions = Object.values(SpecialtyEnum);

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly fb: FormBuilder,
    private readonly doctorService: DoctorService,
    private readonly snackBar: MatSnackBar,
  ) {
    this.form = this.fb.group({
      documentType: ['', Validators.required],
      identificationNumber: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      birthDate: [null as Date | null, Validators.required], // ← Date, no string
      phone: ['', Validators.required],
      gender: ['', Validators.required],
      specialties: [[] as string[], Validators.required],
      canSchedule: [true],
      active: [true],
      intervalStart: ['', Validators.required],
      intervalEnd: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.doctorId = Number(this.route.snapshot.paramMap.get('id'));

    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctorData();
    }
  }

  private loadDoctorData(): void {
    this.doctorService.getDoctorById(this.doctorId).subscribe({
      next: (doctor) => {
        this.form.patchValue({
          documentType: doctor.documentType,
          identificationNumber: doctor.identificationNumber,
          firstName: doctor.firstName,
          lastName: doctor.lastName,
          birthDate: this.parseDateString(doctor.birthDate), // ← convertir a Date
          phone: doctor.phone,
          gender: doctor.gender,
          specialties: doctor.specialties,
          canSchedule: doctor.canSchedule,
          active: doctor.active,
          intervalStart: doctor.appointmentInterval.startTime,
          intervalEnd: doctor.appointmentInterval.endTime,
        });
      },
      error: () => this.openSnackBar('No se pudo cargar la información del médico.', 'error'),
    });
  }

  // Convierte "1990-05-17" → Date para que el datepicker lo entienda
  private parseDateString(dateStr: string): Date | null {
    if (!dateStr) return null;
    // Tomar solo la parte de la fecha "2007-06-19" e ignorar la hora UTC
    const datePart = dateStr.split('T')[0];
    const [year, month, day] = datePart.split('-').map(Number);
    return new Date(year, month - 1, day); // mes es 0-indexed
  }

  // Convierte Date → "1990-05-17" para enviarlo al backend
  private toDateKey(date: Date | string | null): string {
    if (!date) return '';
    if (typeof date === 'string') return date;
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  }

  get specialtiesControl() {
    return this.form.controls.specialties;
  }

  isSpecialtySelected(specialty: string): boolean {
    return (this.specialtiesControl.value ?? []).includes(specialty);
  }

  toggleSpecialty(specialty: string): void {
    const current = [...(this.specialtiesControl.value ?? [])];
    const index = current.indexOf(specialty);
    if (index >= 0) {
      current.splice(index, 1);
    } else {
      current.push(specialty);
    }
    this.specialtiesControl.setValue(current);
  }

  fieldInvalid(field: string): boolean {
    const control = this.form.get(field);
    return !!control && control.invalid && control.touched;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const v = this.form.value;

    const payload: UpdateDoctorDto = {
      id: this.doctorId,
      documentType: v.documentType ?? '',
      identificationNumber: v.identificationNumber ?? '',
      firstName: v.firstName ?? '',
      lastName: v.lastName ?? '',
      birthDate: this.toDateKey(v.birthDate ?? null),
      phone: v.phone ?? '',
      gender: (v.gender as (typeof GENDER_OPTIONS)[number]['value']) ?? 'Otro',
      active: v.active ?? true,
      specialties: v.specialties ?? [],
      canSchedule: v.canSchedule ?? true,
      appointmentInterval: {
        startTime: v.intervalStart ?? '',
        endTime: v.intervalEnd ?? '',
      },
    };

    this.isSaving = true;
    this.doctorService
      .updateDoctor(this.doctorId, payload)
      .pipe(finalize(() => (this.isSaving = false)))
      .subscribe({
        next: () => {
          this.openSnackBar('Médico actualizado correctamente.', 'success');
          this.router.navigate(['/admin/configuracion']);
        },
        error: () => this.openSnackBar('No se pudo actualizar el médico.', 'error'),
      });
  }

  goBack(): void {
    this.router.navigate(['/admin/configuracion']);
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
