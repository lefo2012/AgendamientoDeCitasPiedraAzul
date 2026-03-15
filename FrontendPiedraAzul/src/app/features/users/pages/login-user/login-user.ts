import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login-user.html',
  styleUrl: './login-user.scss'
})
export class Login {

  loginForm: FormGroup;

  constructor(private fb: FormBuilder) {

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

  }

  login() {

    if (this.loginForm.valid) {

      const credentials = this.loginForm.value;

      console.log("Login request:", credentials);

      // aquí luego llamaremos el backend

    }

  }

}