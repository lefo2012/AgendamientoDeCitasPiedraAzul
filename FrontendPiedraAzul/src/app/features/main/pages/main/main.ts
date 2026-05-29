import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreateAppointment } from '../../../appointments/pages/create-appointment/create-appointment';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../users/services/auth.service';
import { AppointmentService } from '../../../appointments/services/appointment.service';



@Component({
  selector: 'app-main',
  imports: [MatDialogModule,CommonModule ],
  templateUrl: './main.html',
  styleUrl: './main.scss',
})
export class Main  {

  private readonly loginRequiredMessage = 'Debes iniciar sesion para agendar una cita.';
  private readonly pendingAppointmentMessage =
    'Ya tienes una cita pendiente. Debes atenderla o cancelarla antes de agendar otra.';

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private authService: AuthService,
    private appointmentService: AppointmentService,
    private snackBar: MatSnackBar
  ) {}

 
  goToAbout() {
    this.router.navigate(['/about']);
  }

  goToCreateAppointment() {
    if (!this.authService.isAuthenticated()) {
      this.snackBar.open(this.loginRequiredMessage, 'Ir al login', {
        duration: 4200,
        horizontalPosition: 'end',
        verticalPosition: 'top',
        panelClass: ['auth-warning-snackbar']
      });
      this.router.navigate(['/login'], {
        queryParams: { message: this.loginRequiredMessage }
      });
      return;
    }

    const patientId = this.resolveCurrentPatientId();
    if (!patientId) {
      this.snackBar.open('No se pudo identificar tu sesion. Inicia sesion nuevamente.', 'Cerrar', {
        duration: 4200,
        horizontalPosition: 'end',
        verticalPosition: 'top',
        panelClass: ['auth-warning-snackbar']
      });
      return;
    }

    this.appointmentService.getPendingAppointments(patientId).subscribe({
      next: (response) => {
        const pendingAppointments = this.normalizePendingAppointments(response);
        if (pendingAppointments.length > 0) {
          this.snackBar.open(this.pendingAppointmentMessage, 'Cerrar', {
            duration: 4200,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['auth-warning-snackbar']
          });
          return;
        }

        this.dialog.open(CreateAppointment, {
          width: '50%',
          height: '50%',
          disableClose: false
        });
      },
      error: () => {
        this.snackBar.open('No fue posible validar tus citas pendientes.', 'Cerrar', {
          duration: 4200,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['auth-warning-snackbar']
        });
      }
    });
  }

  goToRescheduleAppointment() {
    
    this.router.navigate(['/citas/reagendar']);
  }

  goToCancelAppointment() {
    this.router.navigate(['/citas/cancelar']);
    console.log('Navegando a cancelar cita');
  }

  slides = [
  {
    image: '/carrusel/acupuntura.jpeg',
    title: 'Bienvenido a Clínica Piedra Azul',
    description: 'Cuidamos tu salud con medicina alternativa y atención humanizada.'
  },
  {
    image: '/carrusel/masaje.jpeg',
    title: 'Acupuntura',
    description: 'Equilibra tu energía y mejora tu bienestar físico y emocional.'
  },
  {
    image: '/carrusel/medicina.jpeg',
    title: 'Terapia Neural',
    description: 'Tratamientos efectivos para dolores crónicos y regulación del sistema nervioso.'
  }
];

currentSlide = 0;

nextSlide() {
  this.currentSlide = (this.currentSlide + 1) % this.slides.length;
}

prevSlide() {
  this.currentSlide =
    (this.currentSlide - 1 + this.slides.length) % this.slides.length;
}

// (Opcional) autoplay
ngOnInit() {
  setInterval(() => {
    this.nextSlide();
  }, 5000);
}

  private normalizePendingAppointments(response: unknown): any[] {
    if (Array.isArray(response)) {
      return response;
    }

    if (response && typeof response === 'object' && 'pendingAppointments' in response) {
      const pending = (response as { pendingAppointments?: unknown }).pendingAppointments;
      return Array.isArray(pending) ? pending : [];
    }

    return [];
  }

  private resolveCurrentPatientId(): number | null {
    const patient = this.authService.currentPatient();

    if (!patient) {
      return null;
    }

    const candidateIds = [
      patient.id,
      patient['idPatient'],
      patient['patientId']
    ];

    for (const candidate of candidateIds) {
      const parsed = Number(candidate);
      if (Number.isFinite(parsed) && parsed > 0) {
        return parsed;
      }
    }

    return null;
  }
}
