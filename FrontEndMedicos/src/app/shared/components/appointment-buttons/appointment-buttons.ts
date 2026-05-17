import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { AppointmentReportDto } from '../../../features/reports/models/AppointmentReportDto';

@Component({
  selector: 'app-appointment-buttons',
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './appointment-buttons.html',
  styleUrl: './appointment-buttons.scss',
})
export class AppointmentButtons {
  @Input() exportDisabled = false;
  @Input() cancelDisabled = false;
  @Input() rescheduleDisabled = false;
  @Input() selectedAppointment: AppointmentReportDto | null = null;
  @Output() exportRequested = new EventEmitter<void>();
  @Output() cancelRequested = new EventEmitter<void>();
  @Output() rescheduleRequested = new EventEmitter<void>();

  constructor(private router: Router) { }

  goToNewAppointment(): void {
    this.router.navigate(['/citas/nueva']);
  }

  onExportClick(): void {
    this.exportRequested.emit();
  }

  onCancelClick(): void {
    this.cancelRequested.emit();
  }

  onRescheduleClick(): void {
    this.rescheduleRequested.emit();
  }

  goToReschedule(): void {
    console.log('goToReschedule llamado');
    console.log('selectedAppointment:', this.selectedAppointment);
    if (!this.selectedAppointment?.id || !this.selectedAppointment) {
      console.log('→ saliendo por guard');
      return;
    }

    console.log('id Paciente:', this.selectedAppointment.patientId);
    console.log('nombre Paciente:', this.selectedAppointment.patientName);

    this.router.navigate(['/citas/reagendar', this.selectedAppointment.id], {
      queryParams: {
        id: this.selectedAppointment.id,
        idPatient: this.selectedAppointment.patientId,
        patientName: this.selectedAppointment.patientName,
        doctorName: this.selectedAppointment.doctorName,
        date: this.selectedAppointment.date,
        interval: this.selectedAppointment.appointmentInterval,
      }
    });
  }

}