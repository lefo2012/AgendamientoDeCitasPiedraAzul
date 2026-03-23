import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
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
    ReactiveFormsModule,
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
export class AppointmentTable implements OnInit {

  doctors: DoctorDto[] = [];
  appointments: AppointmentReportDto[] = [];
  displayedColumns: string[] = ['doctorName', 'date', 'appointmentInterval', 'patientName'];
  isLoading = false;
  showResults = false;

  filterForm;

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly fb: FormBuilder,
    private readonly reportService: ReportService,
    private readonly scheduleService: ScheduleService,
    private readonly snackBar: MatSnackBar
  ) {
    this.filterForm = this.fb.group({
      doctorId: [null as number | null],
      appointmentDate: [null as Date | null],
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctors();
      // Si quieres que muestre todas las citas al cargar
      this.searchAppointments();
    }
  }

  get doctorIdControl() {
    return this.filterForm.controls.doctorId;
  }

  get appointmentDateControl() {
    return this.filterForm.controls.appointmentDate;
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
    this.isLoading = true;
    this.showResults = false;

    const doctorId = this.doctorIdControl.value;
    const formattedDate = this.formatDateToIso(this.appointmentDateControl.value);

    const obs$ = doctorId && formattedDate
      ? this.reportService.getAppointmentsByDoctorAndDate(doctorId, formattedDate)
      : this.reportService.getAllAppointments();

    obs$
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (data) => {
          // Asegurarse de que la columna "date" exista
          this.appointments = data.map(a => ({
            ...a,
            date: a.date.split('T')[0] // formatear a YYYY-MM-DD si viene con hora
          })).sort((a, b) => a.date.localeCompare(b.date));
          this.showResults = true;
        },
        error: () => {
          this.snackBar.open('Error cargando citas.', 'Cerrar', { duration: 3000 });
          this.appointments = [];
          this.showResults = true;
        },
      });
  }

  resetFilters(): void {
    this.filterForm.reset();
    this.searchAppointments();
  }
}