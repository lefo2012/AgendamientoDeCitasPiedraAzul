import { Component, Inject, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { DoctorDto } from '../../../appointments/models/DoctorDto';
import { ReportService } from '../../services/ReportService';
import { AppointmentReportDto } from '../../models/AppointmentReportDto';
import { ScheduleService } from '../../../appointments/services/schedule.service';
import { FormsModule } from '@angular/forms';
import { AppointmentButtons } from "../../../appointments/pages/appointment-buttons/appointment-buttons";

@Component({
  selector: 'app-appointment-table',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    AppointmentButtons
],
  templateUrl: './appointment-table.html',
  styleUrls: ['./appointment-table.scss'],
})
export class AppointmentTable implements OnInit, OnDestroy {

  doctors: DoctorDto[] = [];
  appointments: AppointmentReportDto[] = [];
  displayedColumns: string[] = ['doctorName', 'date', 'appointmentInterval', 'patientName'];
  showResults = false;
  selectedDoctorId: number | null = null;
  selectedAppointmentDate: Date | null = null;
  private latestRequestId = 0;
  private activeSearchSub?: Subscription;

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly reportService: ReportService,
    private readonly scheduleService: ScheduleService,
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctors();
      this.searchAppointments();
    }
  }

  ngOnDestroy(): void {
    this.activeSearchSub?.unsubscribe();
  }

  loadDoctors(): void {
    this.scheduleService.getAllDoctors().subscribe({
      next: (doctors) => (this.doctors = doctors),
      
      error: () => {
        this.snackBar.open('No se pudieron cargar los doctores.', 'Cerrar', { duration: 3000 });
      },
    });
  }

  private formatDateToIso(date: Date | null): string | null {
    if (!date) return null;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  searchAppointments(): void {
    this.executeSearch(this.selectedDoctorId, this.selectedAppointmentDate);
  }

  private executeSearch(doctorId: number | null, appointmentDate: Date | null): void {
    const formattedDate = this.formatDateToIso(appointmentDate);
    const selectedDoctor = doctorId
      ? this.doctors.find((doctor) => doctor.id === doctorId)
      : null;
    const selectedDoctorName = selectedDoctor
      ? `${selectedDoctor.firstName} ${selectedDoctor.lastName}`.trim().toLowerCase()
      : null;

    const requestId = ++this.latestRequestId;

    const request$ = doctorId && formattedDate
      ? this.reportService.getAppointmentsByDoctorAndDate(doctorId, formattedDate)
      : this.reportService.getAllAppointments();

    this.activeSearchSub?.unsubscribe();
    this.activeSearchSub = request$
      .subscribe({
        next: (data) => {
          if (requestId !== this.latestRequestId) {
            return;
          }

          if (selectedDoctorName) {
            data = data.filter((appointment) =>
              appointment.doctorName.trim().toLowerCase() === selectedDoctorName
            );
          }

          if (formattedDate) {
            data = data.filter((appointment) => {
              const appointmentDate = appointment.date.split('T')[0];
              return appointmentDate === formattedDate;
            });
          }

          this.appointments = data
            .map(a => ({
              ...a,
              date: a.date.split('T')[0]
            }))
            .sort((a, b) => a.date.localeCompare(b.date));
          this.showResults = true;
        },
        error: () => {
          if (requestId !== this.latestRequestId) {
            return;
          }

          this.snackBar.open('Error cargando citas.', 'Cerrar', { duration: 3000 });
          this.appointments = [];
          this.showResults = true;
        },
      });
  }

  resetFilters(): void {
    this.selectedDoctorId = null;
    this.selectedAppointmentDate = null;
    this.executeSearch(null, null);
  }
}