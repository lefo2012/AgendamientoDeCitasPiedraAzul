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
import { MatDialog, MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { Subscription } from 'rxjs';
import { DoctorDto } from '../../../appointments/models/DoctorDto';
import { ReportService } from '../../services/report.service';
import { AppointmentReportDto } from '../../models/AppointmentReportDto';
import { ScheduleService } from '../../../appointments/services/schedule.service';
import { FormsModule } from '@angular/forms';
import { AppointmentButtonsModule } from '../../../../shared/components/appointment-buttons/appointment-buttons.module';

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
    MatDividerModule,
    AppointmentButtonsModule
],
  templateUrl: './appointment-table.html',
  styleUrls: ['./appointment-table.scss'],
})
export class AppointmentTable implements OnInit, OnDestroy {
  @ViewChild('dateInputRef') private dateInputRef?: ElementRef<HTMLInputElement>;

  doctors: DoctorDto[] = [];
  appointments: AppointmentReportDto[] = [];
  displayedColumns: string[] = ['doctorName', 'date', 'appointmentInterval', 'patientName'];
  showResults = false;
  selectedDoctorId: number | null = null;
  selectedAppointmentDate: Date | null = null;
  private latestRequestId = 0;
  private activeSearchSub?: Subscription;
  private pendingSearchTimeout?: ReturnType<typeof setTimeout>;

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: object,
    private readonly reportService: ReportService,
    private readonly scheduleService: ScheduleService,
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

    const headers = ['Doctor', 'Fecha', 'Horario', 'Paciente'];
    const rows = this.appointments.map((appointment) => [
      appointment.doctorName,
      appointment.date,
      appointment.appointmentInterval,
      appointment.patientName,
    ]);

    const csvBody = [headers, ...rows]
      .map((row) => row.map((cell) => this.escapeCsvCell(cell)).join(';'))
      .join('\r\n');

    const csvWithBom = `\uFEFF${csvBody}`;
    const blob = new Blob([csvWithBom], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');

    anchor.href = url;
    anchor.download = this.buildCsvFileName();
    document.body.appendChild(anchor);
    anchor.click();
    document.body.removeChild(anchor);
    window.URL.revokeObjectURL(url);

    this.snackBar.open('CSV exportado correctamente.', 'Cerrar', { duration: 2500 });
  }

  private escapeCsvCell(value: string): string {
    const normalizedValue = `${value ?? ''}`.replace(/"/g, '""');
    return `"${normalizedValue}"`;
  }

  private buildCsvFileName(): string {
    const selectedDoctor = this.selectedDoctorId
      ? this.doctors.find((doctor) => doctor.id === this.selectedDoctorId)
      : null;
    const doctorSegment = selectedDoctor
      ? `${selectedDoctor.firstName}-${selectedDoctor.lastName}`
          .trim()
          .toLowerCase()
          .replace(/\s+/g, '-')
      : 'todos-los-medicos';
    const dateSegment = this.selectedAppointmentDate
      ? this.formatDateToIso(this.selectedAppointmentDate)
      : 'todas-las-fechas';

    return `reporte-citas-${doctorSegment}-${dateSegment}.csv`;
  }
}

// Confirm Export Dialog Component
@Component({
  selector: 'app-confirm-export-dialog',
  standalone: true,
  imports: [MatButtonModule, MatDialogModule, CommonModule, MatDividerModule, MatIconModule],
  template: `
    <div class="export-dialog">
      <div class="export-header">
        <mat-icon class="export-icon">file_download</mat-icon>
        <h2 mat-dialog-title class="export-title">Confirmar Exportación</h2>
      </div>
      <mat-dialog-content>
        <div class="export-summary">
          <span>Total de citas</span>
          <strong>{{ data.appointmentCount }}</strong>
        </div>
        <p class="export-details">El archivo se descargará con el nombre segun los filtros aplicados.</p>
        <mat-divider></mat-divider>
        <p class="export-message">¿Deseas continuar con la descarga?</p>
      </mat-dialog-content>
      <mat-dialog-actions align="end">
        <button mat-button (click)="onCancel()">Cancelar</button>
        <button mat-flat-button color="primary" (click)="onConfirm()">
          <mat-icon>download</mat-icon>
          Descargar
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .export-dialog {
      padding: 8px 0;
    }
    .export-header {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 8px 0;
    }
    .export-icon {
      font-size: 28px;
      width: 28px;
      height: 28px;
      color: var(--azul-medio);
    }
    .export-title {
      margin: 0;
      color: var(--azul-profundo);
      font-size: 20px;
    }
    .export-summary {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 8px 0 12px;
      padding: 12px 14px;
      border-radius: 12px;
      background: var(--fondo-suave);
      border: 1px solid var(--gris-claro);
      font-size: 13px;
      color: var(--gris-oscuro);
    }
    .export-summary strong {
      font-size: 18px;
      color: var(--azul-oscuro);
    }
    .export-details {
      margin: 0 0 12px;
      font-size: 13px;
      color: var(--gris-oscuro);
    }
    .export-message {
      margin: 12px 0 0;
      font-weight: 600;
      color: var(--azul-oscuro);
    }
    mat-dialog-actions {
      gap: 8px;
    }
    button[color="primary"] {
      gap: 8px;
    }
  `]
})
export class ConfirmExportDialog {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private readonly dialogRef: MatDialogRef<ConfirmExportDialog>
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}