import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { AppointmentService } from '../../../appointments/services/appointment.service';
import { PendingAppointment } from '../../../appointments/models/PendingAppointment';
import { ConfirmCancelDialog } from '../../../../shared/dialogs/confirm-cancel-dialog/confirm-cancel-dialog';
import { AuthService } from '../../../users/services/auth.service';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-cancel-appointment',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatProgressBarModule,
    MatDialogModule,
    MatSnackBarModule,
    MatDividerModule,
  ],
  templateUrl: './cancel-appointment.html',
  styleUrl: './cancel-appointment.scss',
})
export class CancelAppointment implements OnInit {
  patientId: number | null = null;
  pendingAppointment: PendingAppointment | null = null;
  isLoading = false;
  appointmentError = '';

  constructor(
    private readonly authService: AuthService,
    private readonly appointmentService: AppointmentService,
    private readonly dialog: MatDialog,
    private readonly snackBar: MatSnackBar,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.patientId = this.resolveCurrentPatientId();
    this.loadPendingAppointment();
    console.log('CancelAppointment component initialized');
  }

  loadPendingAppointment(): void {
    this.appointmentError = '';

    if (!this.patientId) {
      this.appointmentError = 'No se pudo identificar el paciente activo.';
      return;
    }

    this.isLoading = true;

    this.appointmentService.getPendingAppointments(this.patientId).subscribe({
      next: (response) => {
        const list = Array.isArray(response)
          ? response
          : ((response as any)?.pendingAppointments ?? []);
        this.pendingAppointment = list.length > 0 ? list[0] : null;
        this.isLoading = false;
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        this.appointmentError = 'No se pudo cargar la cita pendiente.';
        this.isLoading = false;
        this.cdr.detectChanges(); 
      },
    });
  }

  openCancelDialog(): void {
    if (!this.pendingAppointment) return;

    const dialogRef = this.dialog.open(ConfirmCancelDialog, {
      width: '400px',
      disableClose: false,
      data: {
        appointment: {
          doctorName:
            `${this.pendingAppointment.doctor?.firstName ?? ''} ${this.pendingAppointment.doctor?.lastName ?? ''}`.trim(),
          patientName: '',
          date: this.pendingAppointment.appointmentDate,
          appointmentInterval: `${this.pendingAppointment.interval?.startTime} - ${this.pendingAppointment.interval?.endTime}`,
        },
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.performCancel();
      }
    });
  }

  private performCancel(): void {
    if (!this.pendingAppointment?.id) return;

    this.appointmentService.cancelAppointment(this.pendingAppointment.id).subscribe({
      next: () => {
        this.snackBar.open('Cita cancelada correctamente.', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/']);
      },
      error: () => {
        this.snackBar.open('No se pudo cancelar la cita. Intenta nuevamente.', 'Cerrar', {
          duration: 3000,
        });
      },
    });
  }

  private resolveCurrentPatientId(): number | null {
    const patient = this.authService.currentPatient();

    console.log('Current patient object:', patient);
    if (!patient) {
      return null;
    }

    const candidateIds = [patient.id, patient['idPatient'], patient['patientId']];

    for (const candidate of candidateIds) {
      const parsed = Number(candidate);
      if (Number.isFinite(parsed) && parsed > 0) {
        return parsed;
      }
    }

    return null;
  }
}
