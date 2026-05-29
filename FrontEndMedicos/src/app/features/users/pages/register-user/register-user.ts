import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { AuthService } from '../../services/auth.service';
import { RegisterDoctorRequest } from '../../models/RegisterDoctorRequest';
import { GENDER_OPTIONS } from '../../models/GenderEnum';

@Component({
  selector: 'app-register-doctor',
  templateUrl: './register-user.html',
  standalone: true,
  styleUrls: ['./register-user.scss'],
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatSelectModule
  ]
})
export class RegisterUser {
  registerForm: FormGroup;
  formError = '';
  submitted = false;
  readonly specialtyOptions = ['TERAPIA_NEURAL', 'QUIROPRAXIA', 'FISIOTERAPIA'];
  readonly consultationIntervals = [5, 10, 15, 20, 30, 45, 60];
  readonly genderOptions = GENDER_OPTIONS;
  readonly maxBirthDate: Date;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.maxBirthDate = new Date();

    this.registerForm = this.fb.group(
      {
        firstName: [
          '',
          [
            Validators.required,
            Validators.minLength(2),
            Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$')
          ]
        ],
        lastName: [
          '',
          [
            Validators.required,
            Validators.minLength(2),
            Validators.pattern('^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$')
          ]
        ],
        documentType: ['', Validators.required],
        identificationNumber: [
          '',
          [
            Validators.required,
            Validators.pattern('^[0-9]{6,12}$')
          ]
        ],
        birthDate: [
          '',
          [
            Validators.required,
            this.noFutureDateValidator(),
            this.minimumAgeValidator(0)
          ]
        ],
        phone: [
          '',
          [
            Validators.required,
            Validators.pattern('^[0-9]{10}$')
          ]
        ],
        gender: ['', Validators.required],
        specialties: [[], [Validators.required]],
        canSchedule: [false, Validators.required],
        consultationIntervalMinutes: [30, [Validators.required, Validators.min(5)]],
        email: [
          '',
          [
            Validators.required,
            Validators.email
          ]
        ],
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8),

          ]
        ],
        confirmPassword: ['', Validators.required]
      },
      {
        validators: this.passwordMatchValidator
      }
    );
  }

  registerDoctor(): void {
    this.submitted = true;
    console.groupCollapsed('[RegisterDoctorComponent] Register doctor submit triggered');
    console.log('Form value:', this.registerForm.value);
    console.log('Form valid:', this.registerForm.valid);
    console.groupEnd();

    if (this.registerForm.invalid) {
      console.groupCollapsed('[RegisterDoctorComponent] Register doctor form invalid details');
      Object.keys(this.registerForm.controls).forEach((fieldName) => {
        const control = this.registerForm.get(fieldName);
        if (control?.invalid) {
          console.warn(`Field "${fieldName}" invalid with errors:`, control.errors);
        }
      });
      if (this.registerForm.errors) {
        console.warn('Form-level errors:', this.registerForm.errors);
      }
      console.groupEnd();

      this.registerForm.markAllAsTouched();
      this.formError = 'Corrige los campos del formulario antes de continuar.';
      return;
    }

    const formData = this.registerForm.value;
    const birthDateValue = formData.birthDate instanceof Date
      ? formData.birthDate.toISOString().split('T')[0]
      : formData.birthDate;

    const request: RegisterDoctorRequest = {
      documentType: formData.documentType,
      identificationNumber: formData.identificationNumber,
      firstName: formData.firstName,
      lastName: formData.lastName,
      birthDate: birthDateValue,
      gender: formData.gender,
      phone: formData.phone,
      canSchedule: formData.canSchedule,
      active: true,
      specialties: formData.specialties,
      appointmentInterval: {
        startTime: '00:00',
        endTime: `00:${String(formData.consultationIntervalMinutes).padStart(2, '0')}`
      },
      user: {
        email: formData.email,
        password: formData.password,
        roles: ['MEDICO']
      }
    };

    
    console.groupCollapsed('[RegisterDoctorComponent] Register doctor request payload');
    console.log('Payload sent to backend:', request);
    console.groupEnd();

    this.authService.registerDoctor(request).subscribe({
      next: (response: any) => {
        console.log('[RegisterDoctorComponent] Doctor registration completed successfully.');
        this.formError = '';
        const doctorId = response?.id;
        this.router.navigate(['/configure-schedule'], {
          queryParams: doctorId ? { doctorId } : undefined
        });
      },
      error: (err) => {
        console.error('Doctor registration error', err);
        console.error('Doctor registration error status:', err?.status);
        console.error('Doctor registration error statusText:', err?.statusText);
        console.error('Doctor registration error payload:', err?.error);
        console.error('Doctor registration error URL:', err?.url);

        if (err?.status === 401) {
          console.error('[RegisterDoctorComponent] Backend rejected authentication token (401).');
        } else if (err?.status === 403) {
          console.error('[RegisterDoctorComponent] Authenticated user lacks required role/permission (403).');
        }

        this.formError = 'No se pudo registrar el medico. Intenta de nuevo mas tarde.';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/admin']);
  }

  minimumAgeValidator(minAge: number): ValidatorFn {
    return (control: AbstractControl) => {
      if (!control.value) {
        return null;
      }

      const birthDate = new Date(control.value);
      const today = new Date();

      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDelta = today.getMonth() - birthDate.getMonth();

      if (monthDelta < 0 || (monthDelta === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }

      return age >= minAge ? null : { underAge: true };
    };
  }

  noFutureDateValidator(): ValidatorFn {
    return (control: AbstractControl) => {
      if (!control.value) {
        return null;
      }

      const selected = new Date(control.value);
      const today = new Date();

      selected.setHours(0, 0, 0, 0);
      today.setHours(0, 0, 0, 0);

      return selected > today ? { futureDate: true } : null;
    };
  }

  passwordMatchValidator(form: AbstractControl) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;

    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  fieldInvalid(fieldName: string): boolean {
    const control = this.registerForm.get(fieldName);
    return !!control && control.invalid && (control.touched || this.submitted);
  }
}