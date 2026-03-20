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
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

  }

  login() {

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const credentials = this.loginForm.value;

    this.authService.login(credentials.email, credentials.password)
      .subscribe({
        next: (token) => {
          localStorage.setItem('piedraAzul_access_token', token.access_token);
          this.errorMessage = '';
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('Login error', err);
          this.errorMessage = 'Correo o contraseña inválidos.';
        }
      });
  }

}