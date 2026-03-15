import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AbstractControl, ValidatorFn} from '@angular/forms';
@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.html',
  standalone: true,
  imports: [ReactiveFormsModule],
  styleUrls: ['./register-user.scss']
})
export class RegisterUser {

  registerForm: FormGroup;

  constructor(private fb: FormBuilder) {

    
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

    if (this.registerForm.invalid) {

    this.registerForm.markAllAsTouched();
    return;

    }

    if (this.registerForm.valid) {

      const formData = this.registerForm.value;
      

      const request = {
        documentType: formData.documentType,
        identificationNumber: formData.identificationNumber,
        firstName: formData.firstName,
        lastName: formData.lastName,
        birthDate: formData.birthDate,
        phone: formData.phone,
        active: true,
        user: {
          email: formData.email,
          password: formData.password
        }
      };

}
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