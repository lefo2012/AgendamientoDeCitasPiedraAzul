import { Component } from '@angular/core';
import { Router } from '@angular/router';



@Component({
  selector: 'app-main',
  imports: [],
  templateUrl: './main.html',
  styleUrl: './main.scss',
})
export class Main  {

  constructor(private router: Router) {}

 
  goToAbout() {
    this.router.navigate(['/about']);
  }

  goToCreateAppointment() {
    this.router.navigate(['/citas/agendar']);
  }

  goToRescheduleAppointment() {
    this.router.navigate(['/citas/reagendar']);
  }

  goToCancelAppointment() {
    this.router.navigate(['/citas/cancelar']);
  }

}
