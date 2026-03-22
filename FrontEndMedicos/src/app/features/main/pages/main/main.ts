import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [],
  templateUrl: './main.html',
  styleUrl: './main.scss',
})
export class Main {
  constructor(private router: Router) {}

  goToCreateAppointment() {
    this.router.navigate(['/citas/agendar']);
  }
}
