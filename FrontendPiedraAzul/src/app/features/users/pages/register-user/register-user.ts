import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AbstractControl, ValidatorFn} from '@angular/forms';
import { AuthService, RegisterRequest } from '../../services/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.html',
  standalone: true,
  imports: [ReactiveFormsModule],
  styleUrls: ['./register-user.scss']
})
export class RegisterUser {

  registerForm: FormGroup;
  formError = '';
  private readonly bypassValidationForTesting = true;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {

    
    this.registerForm = this.fb.group({

  firstName: [
    '',
    [
      Validators.required,
      Validators.minLength(2),
      Validators.pattern("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")
    ]
  ],

  lastName: [
    '',
    [
      Validators.required,
      Validators.minLength(2),
      Validators.pattern("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$")
    ]
  ],

  documentType: ['', Validators.required],

  identificationNumber: [
    '',
    [
      Validators.required,
      Validators.pattern("^[0-9]{6,12}$")
    ]
  ],

  birthDate: [
    '',
    [
      Validators.required,
      this.minimumAgeValidator(18)
    ]
  ],

  phone: [
    '',
    [
      Validators.required,
      Validators.pattern("^[0-9]{10}$")
    ]
  ],

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
      Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$")
    ]
  ],

  confirmPassword: ['', Validators.required]

},
{
  validators: this.passwordMatchValidator
});


  }

   register() {
    console.groupCollapsed('[RegisterComponent] Register submit triggered');
    console.log('Form value:', this.registerForm.value);
    console.log('Form valid:', this.registerForm.valid);
    console.groupEnd();

    if (this.registerForm.invalid) {
      console.groupCollapsed('[RegisterComponent] Register form invalid details');
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

      if (!this.bypassValidationForTesting) {
        this.registerForm.markAllAsTouched();
        this.formError = 'Corrija los campos del formulario antes de continuar.';
        return;
      }

      console.warn('[RegisterComponent] bypassValidationForTesting=true, sending request despite invalid form.');
    }

    const formData = this.registerForm.value;

    const request: RegisterRequest = {
      documentType: formData.documentType,
      identificationNumber: formData.identificationNumber,
      firstName: formData.firstName,
      lastName: formData.lastName,
      birthDate: formData.birthDate,
      phone: formData.phone,
      active: true,
      user: {
        email: formData.email,
        password: formData.password,
        roles: ['PACIENTE']
      }
    };

    console.groupCollapsed('[RegisterComponent] Register request payload');
    console.log('Payload sent to backend:', request);
    console.groupEnd();

    this.authService.register(request).subscribe({
      next: () => {
        console.log('[RegisterComponent] Registration completed successfully.');
        this.formError = '';
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Registration error', err);
        console.error('Registration error status:', err?.status);
        console.error('Registration error payload:', err?.error);
        this.formError = 'No se pudo registrar el usuario. Intenta de nuevo más tarde.';
      }
    });
  }

   minimumAgeValidator(minAge: number): ValidatorFn {

  return (control: AbstractControl) => {

    if (!control.value) return null;

    const birthDate = new Date(control.value);
    const today = new Date();

    let age = today.getFullYear() - birthDate.getFullYear();

    const m = today.getMonth() - birthDate.getMonth();

    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    return age >= minAge ? null : { underAge: true };

  };

}
  passwordMatchValidator(form: AbstractControl) {

  const password = form.get('password')?.value;
  const confirmPassword = form.get('confirmPassword')?.value;

  return password === confirmPassword ? null : { passwordMismatch: true };

}



  }