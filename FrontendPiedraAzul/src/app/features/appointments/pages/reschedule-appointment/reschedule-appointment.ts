import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-reschedule-appointment',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatSelectModule
  ],
  templateUrl: './reschedule-appointment.html',
  styleUrl: './reschedule-appointment.scss',
})
export class RescheduleAppointment {
  readonly doctors = [
    'Dra. Valeria Gomez - Medicina General',
    'Dr. Mateo Rios - Fisioterapia',
    'Dra. Laura Diaz - Terapia Neural',
    'Dr. Daniel Patiño - Quiropraxia'
  ];

  readonly timeSlots = [
    '08:00',
    '09:00',
    '10:00',
    '11:00',
    '14:00',
    '15:00',
    '16:00',
    '17:00'
  ];

  readonly minDate = new Date();

  wasSubmitted = false;

  readonly rescheduleForm;

  constructor(private fb: FormBuilder) {
    this.rescheduleForm = this.fb.group({
      appointmentCode: ['', [Validators.required, Validators.minLength(4)]],
      doctor: ['', Validators.required],
      newDate: [null as Date | null, Validators.required],
      newTime: ['', Validators.required],
      reason: ['']
    });
  }

  submitReschedule() {
    if (this.rescheduleForm.invalid) {
      this.rescheduleForm.markAllAsTouched();
      return;
    }

    this.wasSubmitted = true;
  }

  goBack() {
    history.back();
  }
}
