import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-appointment-buttons',
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './appointment-buttons.html',
  styleUrl: './appointment-buttons.scss',
})
export class AppointmentButtons {
  constructor(private router: Router) {}

  goToNewAppointment(): void {
    this.router.navigate(['/citas/nueva']);
  }
}
