import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppointmentReportDto } from '../../../features/reports/models/AppointmentReportDto';
import { AuthService } from '../../../features/users/services/auth.service';
import { NoPermissionDialog } from '../../dialogs/no-permission-dialog/no-permission-dialog';

@Component({
  selector: 'app-appointment-buttons',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatDialogModule],
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

  private readonly noPermissionMessage = 'Tu cuenta no tiene permisos para gestionar citas.';

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private authService: AuthService
  ) { }

  get canSchedule(): boolean {
    return this.authService.canScheduleAppointments();
  }

  goToNewAppointment(): void {
    if (!this.canSchedule) {
      this.openPermissionMessage();
      return;
    }
    this.router.navigate(['/citas/nueva']);
  }

  onExportClick(): void {
    this.exportRequested.emit();
  }

  onCancelClick(): void {
    if (!this.canSchedule) {
      this.openPermissionMessage();
      return;
    }
    this.cancelRequested.emit();
  }

  onRescheduleClick(): void {
    if (!this.canSchedule) {
      this.openPermissionMessage();
      return;
    }
    this.rescheduleRequested.emit();
  }

  goToReschedule(): void {
    if (!this.canSchedule) {
      this.openPermissionMessage();
      return;
    }
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

  private openPermissionMessage(): void {
    this.dialog.open(NoPermissionDialog, {
      width: '420px',
      data: { message: this.noPermissionMessage }
    });
  }

}