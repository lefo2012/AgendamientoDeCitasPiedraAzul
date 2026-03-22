import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-cancel-appointment',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './cancel-appointment.html',
  styleUrl: './cancel-appointment.scss',
})
export class CancelAppointment {
  readonly cancellationReasons = [
    'No puedo asistir',
    'Ya no requiero la consulta',
    'Cita duplicada',
    'Otro motivo'
  ];

  wasSubmitted = false;

  readonly cancelForm;

  constructor(private fb: FormBuilder) {
    this.cancelForm = this.fb.group({
      appointmentCode: ['', [Validators.required, Validators.minLength(4)]],
      reason: ['', Validators.required],
      details: ['']
    });
  }

  submitCancellation() {
    if (this.cancelForm.invalid) {
      this.cancelForm.markAllAsTouched();
      return;
    }

    this.wasSubmitted = true;
  }

  goBack() {
    history.back();
  }
}
