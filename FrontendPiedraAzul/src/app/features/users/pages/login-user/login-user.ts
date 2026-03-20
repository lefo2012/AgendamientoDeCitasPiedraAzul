import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login-user.html',
  styleUrl: './login-user.scss'
})
export class Login {

  loginForm: FormGroup;
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {

    this.loginForm = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', Validators.required]
    });

  }

  login() {
    console.groupCollapsed('[LoginComponent] Login submit triggered');
    console.log('Form value:', this.loginForm.value);
    console.log('Form valid:', this.loginForm.valid);
    console.groupEnd();

    if (this.loginForm.invalid) {
      console.warn('[LoginComponent] Login form is invalid. Validation prevented request.');
      this.loginForm.markAllAsTouched();
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

          localStorage.setItem('piedraAzul_access_token', token.access_token);
          console.log('[LoginComponent] Token stored in localStorage key: piedraAzul_access_token');

          this.errorMessage = '';
          this.router.navigate(['/']);
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

}