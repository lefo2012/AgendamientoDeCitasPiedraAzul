import { ChangeDetectorRef, Component, ElementRef, Inject, OnDestroy, OnInit, PLATFORM_ID, ViewChild } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepicker, MatDatepickerInputEvent, MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Subscription } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { DoctorDto } from '../../../appointments/models/DoctorDto';
import { ReportService } from '../../services/report.service';
import { AppointmentService } from '../../../appointments/services/appointment.service';
import { AppointmentReportDto } from '../../models/AppointmentReportDto';
import { DoctorService } from '../../../appointments/services/doctor.service';
import { FormsModule } from '@angular/forms';
import { AppointmentButtonsModule } from '../../../../shared/components/appointment-buttons/appointment-buttons.module';
import { ConfirmExportDialog } from '../../../../shared/dialogs/confirm-export-dialog/confirm-export-dialog';
import { ConfirmCancelDialog } from '../../../../shared/dialogs/confirm-cancel-dialog/confirm-cancel-dialog';

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
    MatDialogModule,
    MatIconModule,
    MatCheckboxModule,
    AppointmentButtonsModule
],
  templateUrl: './appointment-table.html',
  styleUrls: ['./appointment-table.scss'],
})
export class AppointmentTable implements OnInit, OnDestroy {
  @ViewChild('dateInputRef') private dateInputRef?: ElementRef<HTMLInputElement>;
  doctors: DoctorDto[] = [];
  appointments: AppointmentReportDto[] = [];
  displayedColumns: string[] = ['select', 'doctorName', 'date', 'appointmentInterval', 'patientName'];
  showResults = false;
  selectedDoctorId: number | null = null;
  selectedAppointmentDate: Date | null = null;
  selectedAppointmentId: number | null = null;
  selectedAppointment: AppointmentReportDto | null = null;
  selection = new SelectionModel<AppointmentReportDto>(false, []);
  private latestRequestId = 0;
  private activeSearchSub?: Subscription;
  private pendingSearchTimeout?: ReturnType<typeof setTimeout>;

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly appointmentService: AppointmentService,
    private readonly reportService: ReportService,
    private readonly doctorService: DoctorService,
    private readonly snackBar: MatSnackBar,
    private readonly cdr: ChangeDetectorRef,
    private readonly dialog: MatDialog
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDoctors();
      this.searchAppointments();
    }
  }

  ngOnDestroy(): void {
    this.activeSearchSub?.unsubscribe();
    if (this.pendingSearchTimeout) {
      clearTimeout(this.pendingSearchTimeout);
      this.pendingSearchTimeout = undefined;
    }
  }

  loadDoctors(): void {
  this.doctorService.getAllDoctors().subscribe({
    next: (doctors) => {
      setTimeout(() => {
        this.doctors = doctors;
        this.cdr.detectChanges();
      }, 0);
    },
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

  onDoctorFilterChange(): void {
    this.queueSearch();
  }

  onDateFilterChange(event: MatDatepickerInputEvent<Date>, picker: MatDatepicker<Date>): void {
    this.selectedAppointmentDate = event.value ?? null;
    picker.close();
    this.dateInputRef?.nativeElement.blur();
    this.queueSearch();
  }

  onSearchClick(): void {
    this.searchAppointments();
  }

  private queueSearch(): void {
    if (this.pendingSearchTimeout) {
      clearTimeout(this.pendingSearchTimeout);
    }

    this.pendingSearchTimeout = setTimeout(() => {
      this.executeSearch(this.selectedDoctorId, this.selectedAppointmentDate);
      this.pendingSearchTimeout = undefined;
    }, 0);
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
          console.log('Primera cita del backend:', data[0]);
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

          const normalizedData = data
            .map(a => ({
              ...a,
              date: a.date.split('T')[0]
            }))
            .sort((a, b) => a.date.localeCompare(b.date));

          setTimeout(() => {
            if (requestId !== this.latestRequestId) {
              return;
            }

            this.appointments = normalizedData;
            if (!this.selectedAppointmentId) {
              this.setSelectedAppointment(null);
            } else {
              const matching = this.appointments.find(a => a.id === this.selectedAppointmentId) ?? null;
              this.setSelectedAppointment(matching);
            }
            this.showResults = true;
            this.cdr.detectChanges();
          }, 0);
        },
        error: () => {
          if (requestId !== this.latestRequestId) {
            return;
          }

          this.snackBar.open('Error cargando citas.', 'Cerrar', { duration: 3000 });

          setTimeout(() => {
            if (requestId !== this.latestRequestId) {
              return;
            }

            this.appointments = [];
            this.showResults = true;
            this.cdr.detectChanges();
          }, 0);
        },
      });
  }

  resetFilters(): void {
    this.latestRequestId++;
    this.activeSearchSub?.unsubscribe();

    if (this.pendingSearchTimeout) {
      clearTimeout(this.pendingSearchTimeout);
      this.pendingSearchTimeout = undefined;
    }

    this.selectedDoctorId = null;
    this.selectedAppointmentDate = null;
    this.setSelectedAppointment(null);
    this.dateInputRef?.nativeElement.blur();

    setTimeout(() => {
      this.executeSearch(null, null);
    }, 0);
  }

  showAppointmentsForToday(): void {
    const today = new Date();
    this.selectedAppointmentDate = today;
    this.selectedDoctorId = null;
    this.dateInputRef?.nativeElement.blur();
    this.queueSearch();
  }

  exportCurrentResultsToCsv(): void {
    if (!this.appointments.length) {
      this.snackBar.open('No hay citas para exportar.', 'Cerrar', { duration: 3000 });
      return;
    }

    const dialogRef = this.dialog.open(ConfirmExportDialog, {
      width: '400px',
      disableClose: false,
      data: {
        appointmentCount: this.appointments.length
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.performCsvExport();
      }
    });
  }

  private performCsvExport(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    const exportDate = this.selectedAppointmentDate ?? new Date();
    const formattedDate = this.formatDateToIso(exportDate);

    if (!formattedDate) {
      this.snackBar.open('No se pudo determinar la fecha para exportar.', 'Cerrar', { duration: 3000 });
      return;
    }

    this.reportService.exportAppointmentsCsv(this.selectedDoctorId, formattedDate).subscribe({
      next: (csvBlob) => {
        const url = window.URL.createObjectURL(csvBlob);
        const anchor = document.createElement('a');

        anchor.href = url;
        anchor.download = this.buildCsvFileName();
        document.body.appendChild(anchor);
        anchor.click();
        document.body.removeChild(anchor);
        window.URL.revokeObjectURL(url);

        this.snackBar.open('CSV exportado correctamente.', 'Cerrar', { duration: 2500 });
      },
      error: () => {
        this.snackBar.open('No se pudo exportar el CSV.', 'Cerrar', { duration: 3000 });
      }
    });
  }

  private buildCsvFileName(): string {
    const selectedDoctor = this.selectedDoctorId
      ? this.doctors.find((doctor) => doctor.id === this.selectedDoctorId)
      : null;
    const doctorSegment = selectedDoctor
      ? `${selectedDoctor.firstName}-${selectedDoctor.lastName}`
          .trim()
          .replace(/\s+/g, '-')
      : 'Todos-Los-Doctores';
    const dateSegment = this.formatDateToIso(this.selectedAppointmentDate ?? new Date());

    return `Lista-Citas-${doctorSegment}-${dateSegment}.csv`;
  }

  cancelAppointment(): void {
    if (!this.selectedAppointmentId || !this.selectedAppointment) {
      return;
    }
     console.log('cancelAppointment llamado');
  console.log('selectedAppointmentId:', this.selectedAppointmentId);
  console.log('selectedAppointment:', this.selectedAppointment);
    const dialogRef = this.dialog.open(ConfirmCancelDialog, {
      width: '400px',
      disableClose: false,
      data: {
        appointment: {
          doctorName: this.selectedAppointment.doctorName,
          patientName: this.selectedAppointment.patientName,
          date: this.selectedAppointment.date,
          appointmentInterval: this.selectedAppointment.appointmentInterval
        }
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.performCancelAppointment();
      }
    });
  }

  private performCancelAppointment(): void {
    if (!this.selectedAppointmentId) {
      return;
    }

    this.appointmentService.cancelAppointment(this.selectedAppointmentId).subscribe({
      next: () => {
        this.snackBar.open('Cita cancelada correctamente.', 'Cerrar', { duration: 2500 });
        this.selectedAppointmentId = null;
        this.selectedAppointment = null;
        this.searchAppointments();
      },
      error: () => {
        this.snackBar.open('No se pudo cancelar la cita.', 'Cerrar', { duration: 3000 });
      }
    });
  }

  get hasSelection(): boolean {
    return this.selection.hasValue();
  }

  //selectAppointment(appointment: AppointmentReportDto): void {
   // this.setSelectedAppointment(appointment);
  //}

 toggleSelection(appointment: AppointmentReportDto): void {
  console.log('toggleSelection llamado, id:', appointment.id);
  console.log('selectedAppointmentId actual:', this.selectedAppointmentId);
  
  if (this.selectedAppointmentId === appointment.id) {
    console.log('→ deseleccionando');
    this.setSelectedAppointment(null);
    return;
  }
  console.log('→ seleccionando');
  this.setSelectedAppointment(appointment);
}

  private setSelectedAppointment(appointment: AppointmentReportDto | null): void {
  console.log('setSelectedAppointment llamado con:', appointment?.id ?? null);
  this.selection.clear();
  if (!appointment) {
    this.selectedAppointmentId = null;
    this.selectedAppointment = null;
    return;
  }
  this.selection.select(appointment);
  this.selectedAppointmentId = appointment.id;
  this.selectedAppointment = appointment;
  console.log('selectedAppointmentId ahora es:', this.selectedAppointmentId);
}
}