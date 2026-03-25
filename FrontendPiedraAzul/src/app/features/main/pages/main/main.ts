import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreateAppointment } from '../../../appointments/pages/create-appointment/create-appointment';
import { CommonModule } from '@angular/common';



@Component({
  selector: 'app-main',
  imports: [MatDialogModule,CommonModule ],
  templateUrl: './main.html',
  styleUrl: './main.scss',
})
export class Main  {

  constructor(private router: Router, private dialog: MatDialog) {}

 
  goToAbout() {
    this.router.navigate(['/about']);
  }

  goToCreateAppointment() {
    this.dialog.open(CreateAppointment, {
      width: '50%',
      height: '50%',
      disableClose: false
    });
  }

  goToRescheduleAppointment() {
    this.router.navigate(['/citas/reagendar']);
  }

  goToCancelAppointment() {
    this.router.navigate(['/citas/cancelar']);
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
}
