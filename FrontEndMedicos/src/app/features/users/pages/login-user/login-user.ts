import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  templateUrl: './login-user.html',
  styleUrl: './login-user.scss',
})
export class Login {
  loginForm: FormGroup;
  errorMessage = '';
  submitted = false;

  constructor(private fb: FormBuilder, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', Validators.required],
    });
  }

  login() {
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.errorMessage = 'Completa correctamente los campos para continuar.';
      return;
    }

    this.errorMessage = '';
    this.router.navigate(['/']);
  }

  fieldInvalid(fieldName: 'email' | 'password'): boolean {
    const control = this.loginForm.get(fieldName);
    return !!control && control.invalid && (control.touched || this.submitted);
  }
}
