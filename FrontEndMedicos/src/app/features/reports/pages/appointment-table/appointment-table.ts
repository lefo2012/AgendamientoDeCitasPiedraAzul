import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentButtons } from '../../../appointments/pages/appointment-buttons/appointment-buttons';
import { ReportService } from '../../services/ReportService';
import { AppointmentReportDto } from '../../models/AppointmentReportDto';
import { ScheduleService } from '../../../appointments/services/schedule.service';
import { DoctorDto } from '../../../appointments/models/DoctorDto';

interface Appointment {
  doctor: string;
  date: string;
  timeRange: string;
  patient: string;
}

@Component({
  selector: 'app-appointment-table',
  standalone: true,
  imports: [CommonModule, FormsModule, AppointmentButtons],
  templateUrl: './appointment-table.html',
  styleUrls: ['./appointment-table.scss'],
})
export class AppointmentTable implements OnInit {

  appointments: Appointment[] = [];
  doctors: DoctorDto[] = [];

  selectedDoctorId: number | null = null;
  selectedDate: string = '';

  isLoading = false;

  constructor(
    private reportService: ReportService,
    private scheduleService: ScheduleService
  ) {}

  ngOnInit(): void {
    this.loadDoctors();
    this.loadAllAppointments();
  }

  // Cargar doctores para el filtro
  loadDoctors(): void {
    this.scheduleService.getAllDoctors().subscribe({
      next: (data) => this.doctors = data,
      error: () => console.error('Error cargando doctores')
    });
  }

  // Cargar todas las citas por defecto
  loadAllAppointments(): void {
    this.isLoading = true;
    this.reportService.getAllAppointments().subscribe({
      next: (data: AppointmentReportDto[]) => {
        this.appointments = data.map(item => ({
          doctor: item.doctorName,
          date: '', // si quieres puedes extraer fecha del intervalo
          timeRange: item.appointmentInterval,
          patient: item.patientName
        }));
      },
      error: () => console.error('Error cargando citas'),
      complete: () => this.isLoading = false
    });
  }

  // Buscar con filtros
  searchAppointments(): void {
    if (this.selectedDoctorId && this.selectedDate) {
      this.isLoading = true;
      this.reportService
        .getAppointmentsByDoctorAndDate(this.selectedDoctorId, this.selectedDate)
        .subscribe({
          next: (data: AppointmentReportDto[]) => {
            this.appointments = data.map(item => ({
              doctor: item.doctorName,
              date: this.selectedDate,
              timeRange: item.appointmentInterval,
              patient: item.patientName
            }));
          },
          error: () => console.error('Error cargando citas filtradas'),
          complete: () => this.isLoading = false
        });
    } else {
      // Si no hay filtros, mostrar todas
      this.loadAllAppointments();
    }
  }

  // Reset filtros
  resetFilters(): void {
    this.selectedDoctorId = null;
    this.selectedDate = '';
    this.loadAllAppointments();
  }
}