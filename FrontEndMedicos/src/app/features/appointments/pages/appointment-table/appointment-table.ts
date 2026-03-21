import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentButtons } from '../appointment-buttons/appointment-buttons';

interface Appointment {
  doctor: string;
  date: string;
  timeRange: string;
  patient: string;
}

@Component({
  selector: 'app-appointment-table',
  standalone: true,
  imports: [CommonModule, AppointmentButtons],
  templateUrl: './appointment-table.html',
  styleUrls: ['./appointment-table.scss'],
})
export class AppointmentTable {
  appointments: Appointment[] = [
    { doctor: 'Dr. Luis Fierro', date: '2026-03-25', timeRange: '09:00 - 09:30', patient: 'Morticio Adams' },
    { doctor: 'Dra. Luis Fierro', date: '2026-03-25', timeRange: '10:00 - 10:30', patient: 'Nelson Rosales' },
    { doctor: 'Dr. Luis Fierro', date: '2026-03-26', timeRange: '11:00 - 11:30', patient: 'Luisa Mendez' },
  ];
}
