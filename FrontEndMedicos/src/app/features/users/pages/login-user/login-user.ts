import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './login-user.html',
  styleUrl: './login-user.scss'
})
export class Login {
  loginForm: FormGroup;
  errorMessage = '';
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const routeMessage = this.route.snapshot.queryParamMap.get('message');

    if (routeMessage) {
      this.errorMessage = routeMessage;
    }
  }

  login() {
    this.submitted = true;
    this.errorMessage = '';
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

    this.authService.login(email, credentials.password)
      .subscribe({
        next: (token) => {
          console.groupCollapsed('[LoginComponent] Login success');
          console.log('Token response from service:', token);
          console.log('Access token received:', token?.access_token ?? '(none)');
          console.groupEnd();
          const roles = this.authService.getRolesFromToken(token.access_token);

          console.log('[LoginComponent] Roles extracted:', roles);

          if (roles.includes('ADMIN')) {
            this.authService.startSessionWithToken(token);
            this.errorMessage = '';
            console.log('[LoginComponent] Redirecting to /admin');
            this.router.navigate(['/admin']);
          } else {
            this.authService.initializeSession(token).subscribe({
              next: (doctor) => {
                if (!doctor) {
                  this.authService.clearSession();
                  this.errorMessage = 'No se pudo recuperar la informacion del medico. Inicia sesion nuevamente.';
                  return;
                }

                this.errorMessage = '';
                console.log('[LoginComponent] Redirecting to /');
                this.router.navigate(['/']);
              },
              error: (sessionError) => {
                console.error('[LoginComponent] Could not load doctor profile from backend.', sessionError);

                if (sessionError?.status === 401) {
                  this.errorMessage = 'Token valido, pero no fue posible cargar el perfil del doctor.';
                  return;
                }

                if (sessionError?.status === 0) {
                  this.errorMessage = 'No fue posible conectar con el backend para cargar el perfil del doctor.';
                  return;
                }

                this.errorMessage = 'No se pudo cargar la informacion del doctor.';
              }
            });
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

          if (err?.status === 0) {
            this.errorMessage = 'No hubo conexión con Keycloak (CORS, SSL o servidor apagado).';
            return;
          }

          this.errorMessage = 'Error de autenticación. Revisa la consola para más detalle.';
        }
      });
  }

  fieldInvalid(fieldName: 'email' | 'password'): boolean {
    const control = this.loginForm.get(fieldName);
    return !!control && control.invalid && (control.touched || this.submitted);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}