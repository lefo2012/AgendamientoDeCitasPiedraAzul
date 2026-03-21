import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { AuthService, RegisterDoctorRequest } from '../../services/auth.service';

@Component({
  selector: 'app-register-doctor',
  templateUrl: './register-doctor.html',
  styleUrls: ['./register-doctor.scss'],
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatSelectModule
  ]
})
export class RegisterDoctor {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly specialtyOptions = ['Terapia Neural', 'Quiropraxia', 'Fisioterapia'];
  readonly maxBirthDate: Date = new Date();

  registerForm: FormGroup = this.fb.group(
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
      specialties: [[], [Validators.required]],
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
          Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$')
        ]
      ],
      confirmPassword: ['', Validators.required]
    },
    {
      validators: this.passwordMatchValidator
    }
  );

  formError = '';
  submitted = false;

  registerDoctor(): void {
    this.submitted = true;

    if (this.registerForm.invalid) {
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
      phone: formData.phone,
      active: true,
      specialties: formData.specialties,
      user: {
        email: formData.email,
        password: formData.password,
        roles: ['MEDICO']
      }
    };

    this.authService.registerDoctor(request).subscribe({
      next: () => {
        this.formError = '';
        this.router.navigate(['/admin']);
      },
      error: () => {
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
