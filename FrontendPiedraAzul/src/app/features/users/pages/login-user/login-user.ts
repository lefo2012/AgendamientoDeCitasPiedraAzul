import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators,ValidatorFn,AbstractControl} from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RegisterRequest } from '../../models/RegisterRequest';
import { GENDER_OPTIONS } from '../../models/GenderEnum';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { map, switchMap } from 'rxjs';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule
  ],
  templateUrl: './login-user.html',
  styleUrl: './login-user.scss'
})
export class Login {
  loginActive = true;
  registerActive = false;
  loginForm: FormGroup;
  errorMessage = '';
  submitted = false;
  registerForm: FormGroup;
  formError = '';
  readonly genderOptions = GENDER_OPTIONS;
  readonly maxBirthDate: Date;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', Validators.required]
    });
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
           // Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$')
          ]
        ],
        confirmPassword: ['', Validators.required]
      },
      {
        validators: this.passwordMatchValidator
      }
    );

  }

  login() {
    this.submitted = true;
    console.groupCollapsed('[LoginComponent] Login submit triggered');
    console.log('Form value:', this.loginForm.value);
    console.log('Form valid:', this.loginForm.valid);
    console.groupEnd();

    if (this.loginForm.invalid) {
      console.warn('[LoginComponent] Login form is invalid. Validation prevented request.');
      this.loginForm.markAllAsTouched();
      this.errorMessage = 'Completa correctamente los campos para continuar.';
      return;
    }

    const credentials = this.loginForm.value;
    const email = credentials.email?.trim?.() ?? credentials.email;


    console.log('[LoginComponent] Calling AuthService.login for user:', email);

    this.authService.login(email, credentials.password).pipe(
      switchMap((token) =>
        this.authService.initializeSession(token).pipe(
          map((patient) => ({ token, patient }))
        )
      )
    )
      .subscribe({
        next: ({ token, patient }) => {
          console.groupCollapsed('[LoginComponent] Login success');
          console.log('Token response from service:', token);
          console.log('Access token received:', token?.access_token ?? '(none)');
          console.log('Current patient from /api/auth/me:', patient);
          console.groupEnd();

          this.errorMessage = '';
          const roles = this.authService.getRolesFromToken(token.access_token);

          console.log('[LoginComponent] Roles extracted:', roles);

          if (roles.includes('admin')) {
            console.log('[LoginComponent] Redirecting to /admin');
            this.router.navigate(['/admin']);
          } else {
            console.log('[LoginComponent] Redirecting to /');
            this.router.navigate(['/']);
          }





        },
        error: (err) => {
          console.groupCollapsed('[LoginComponent] Login failed');
          console.error('Error object:', err);
          console.error('Status:', err?.status);
          console.error('Backend payload:', err?.error);
          console.groupEnd();

          if (err?.status === 400 && err?.error?.error === 'invalid_grant') {
            this.errorMessage = 'Credenciales incorrectas (invalid_grant).';
            return;
          }

          if (err?.status === 401) {
            this.errorMessage = 'Token válido, pero no fue posible cargar el perfil del paciente.';
            return;
          }

          if (err?.status === 0) {
            this.errorMessage = 'No hubo conexión con Keycloak (CORS, SSL o servidor apagado).';
            return;
          }

          this.errorMessage = 'Error de autenticación. Revisa la consola para más detalle.';
        }
      });
  }

  fieldInvalidLogin(fieldName: 'email' | 'password'): boolean {
    const control = this.loginForm.get(fieldName);
    return !!control && control.invalid && (control.touched || this.submitted);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }



  register() {
    this.submitted = true;
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

      this.registerForm.markAllAsTouched();
      this.formError = 'Corrige los campos del formulario antes de continuar.';
      return;
    }

    const formData = this.registerForm.value;
    const birthDateValue = formData.birthDate instanceof Date
      ? formData.birthDate.toISOString().split('T')[0]
      : formData.birthDate;

    const request: RegisterRequest = {
      documentType: formData.documentType,
      identificationNumber: formData.identificationNumber,
      firstName: formData.firstName,
      lastName: formData.lastName,
      birthDate: birthDateValue,
      gender: formData.gender,
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

  fieldInvalidRegister(fieldName: string): boolean {
    const control = this.registerForm.get(fieldName);
    return !!control && control.invalid && (control.touched || this.submitted);
  }
  changeToRegister() {
    this.loginActive = false;
    this.registerActive = true;
  }
  changeToLogin() {
    this.loginActive = true;
    this.registerActive = false;
  }
}