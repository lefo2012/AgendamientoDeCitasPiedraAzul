import { Component, Optional } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButton } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { AppointmentService } from '../../services/appointment.service';
import { DoctorService } from '../../services/doctor.service';
import { DoctorDto } from '../../models/DoctorDto';
import { IntervalDto } from '../../models/IntervalDto';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatInput } from '@angular/material/input';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';
import { ReserveAppointmentDto } from '../../models/ReserveAppointmentDto';
import { AuthService } from '../../../users/services/auth.service';
import { CurrentPatient } from '../../../users/models/CurrentPatient';
@Component({
  selector: 'app-create-appointment',
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatDialogModule,
    MatButton,
    MatTooltipModule,
    MatProgressBarModule,
    MatDatepicker,
    MatInput,
    MatNativeDateModule,
  ],
  templateUrl: './create-appointment.html',
  styleUrl: './create-appointment.scss',
})
export class CreateAppointment {
  reservationError = '';
  minDate = new Date();

  get currentPatient(): CurrentPatient | null {
    return this.authService.currentPatient();
  }

  get doctorDisplayName(): string {
    const doctor = this.doctorSelected;
    if (!doctor) {
      return '';
    }
    return `${doctor.firstName} ${doctor.lastName}`;
  }

  get currentPatientDisplayName(): string {
    const patient = this.currentPatient;
    if (!patient) {
      return '';
    }

    const firstName = `${patient.firstName ?? ''}`.trim();
    const lastName = `${patient.lastName ?? ''}`.trim();
    const fullName = `${firstName} ${lastName}`.trim();

    return fullName || `${patient.email ?? ''}`.trim() || 'Paciente';
  }

  private resolveCurrentPatientId(): number | null {
    const patient = this.currentPatient;

    if (!patient) {
      return null;
    }

    const candidateIds = [patient.id, patient['idPatient'], patient['patientId']];

    for (const candidate of candidateIds) {
      const parsed = Number(candidate);
      if (Number.isFinite(parsed) && parsed > 0) {
        return parsed;
      }
    }

    return null;
  }

  reserveAppointment: ReserveAppointmentDto = {
    idPatient: 0,
    idDoctor: 0,
    interval: {
      startTime: '',
      endTime: '',
    },
    appointmentDate: '',
  };

  appointmentForm: any;

  specialitySelected: any | null = null;

  doctorSelected: DoctorDto = {
    id: 0,
    identificationNumber: '',
    firstName: '',
    lastName: '',
    canSchedule: false,
    appointmentInterval: null,
    schedule: { availableTimes: {} },
  };

  dateSelected: any | null = null;
  intervalSelected: any | null = null;

  hoveredSpeciality: any | null = null;
  step = 0;
  maxSteps = 5;
  doctors: DoctorDto[] = [];
  intervals: any[] = [];
  dates: string[] = [];

  constructor(
    private readonly appointmentService: AppointmentService,
    private readonly doctorService: DoctorService,
    private fb: FormBuilder,
    private readonly authService: AuthService,
    @Optional() private dialogRef: MatDialogRef<CreateAppointment> | null,
    private snackBar: MatSnackBar,
  ) {}

  specialties: any = [
    {
      value: 'CONSULTA_GENERAL',
      viewValue: 'Consulta General',
      description:
        'Si es tu primera vez, selecciona esta opción para que el equipo médico te valore y te asigne el tratamiento adecuado.',
    },
    {
      value: 'FISIOTERAPIA',
      viewValue: 'Fisioterapia',
      description:
        'La fisioterapia sirve para rehabilitar, prevenir y tratar lesiones físicas, dolor crónico y problemas de movilidad',
    },
    {
      value: 'TERAPIA_NEURAL',
      viewValue: 'Terapia Neural',
      description: 'La terapia neural busca aliviar el dolor crónico y enfermedades funcionales',
    },
    {
      value: 'QUIROPRAXIA',
      viewValue: 'Quiropraxia',
      description:
        'La quiropraxia es una especialidad que se enfoca en la rehabilitación de lesiones y enfermedades musculoesqueleticas',
    },
  ];

  /**
   * Submit the current step and go to the next one.
   * If the current step is 0 and the specialty is not selected, it will stay in the same step.
   */
  next() {
    if (this.step === 0 && this.specialitySelected === null) {
      this.step = 0;
    } else if (this.step === 1 && this.doctorSelected?.id === 0) {
      this.step = 1;
    } else if (this.step === 2 && this.dateSelected === null) {
      this.step = 2;
    } else if (this.step === 3 && this.intervalSelected === null) {
      this.step = 3;
    } else {
      if (this.step < this.maxSteps) {
        this.step++;
      }
    }
  }

  previous() {
    if (this.step > 0) {
      this.step--;
    }
  }

  onBack() {
    if (this.step >= 1) {
      this.step--;
    } else {
      this.step = 0;
    }
  }
  panelX = 0;
  panelY = 0;

  onSpecialityChange() {
    if (!this.specialitySelected) {
      this.doctors = [];
      return;
    }

    const specialtyValue =
      this.specialitySelected.value === 'CONSULTA_GENERAL'
        ? 'TERAPIA_NEURAL'
        : this.specialitySelected.value;

    this.doctorService
      .getDoctorsBySpeciality(specialtyValue)
      .subscribe((doctors) => {
        this.doctors = doctors;
      });
  }
  onDoctorChange() {
    setTimeout(() => {
      this.dates = Object.keys(this.doctorSelected?.schedule?.availableTimes || {});
    });
  }

  dateFilter = (date: Date | null): boolean => {
    if (!date) return false;

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    const formatted = `${year}-${month}-${day}`;
    return this.dates.includes(formatted);
  };

  onDateChange(event: any) {
    console.log(event);

    if (!event.value) return;

    const date = event.value as Date;
    const formatted = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    this.dateSelected = formatted;
    console.log('Fecha seleccionada:', formatted);

    const daySchedule = this.doctorSelected.schedule?.availableTimes[formatted]?.intervals || [];

    if (!this.doctorSelected.appointmentInterval) {
      this.intervals = daySchedule.map((block) => ({
        startTime: block.startTime.slice(0, 5),
        endTime: block.endTime.slice(0, 5),
      }));
      return;
    }

    const minutes =
      this.toMinutes(this.doctorSelected.appointmentInterval.endTime) -
      this.toMinutes(this.doctorSelected.appointmentInterval.startTime);

    const result: IntervalDto[] = [];

    daySchedule.forEach((block: IntervalDto) => {
      let start = this.toMinutes(block.startTime);
      const end = this.toMinutes(block.endTime);

      while (start + minutes <= end) {
        result.push({
          startTime: this.toTime(start), // HH:MM
          endTime: this.toTime(start + minutes),
        });
        start += minutes;
      }
    });

    this.intervals = result;
  }

  toMinutes(time: string): number {
    const [h, m] = time.split(':').map(Number);
    return h * 60 + m;
  }

  toTime(minutes: number): string {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
  }

  onHover(event: MouseEvent | null, speciality: any | null) {
    this.hoveredSpeciality = speciality;

    if (event && event.target) {
      const rect = (event.target as HTMLElement).getBoundingClientRect();

      this.panelX = rect.right + 10;
      this.panelY = rect.top;
    }
  }

  get progress(): number {
    return (this.step / this.maxSteps) * 100;
  }

  onCancel() {
    this.dialogRef?.close();
  }

  confirmAppointment() {
    const currentPatientId = this.resolveCurrentPatientId();

    if (!currentPatientId) {
      this.reservationError = 'No se pudo identificar tu sesion. Inicia sesion nuevamente.';
      return;
    }

    this.reserveAppointment = {
      idPatient: currentPatientId,
      idDoctor: this.doctorSelected.id,
      interval: this.intervalSelected,
      appointmentDate: this.dateSelected,
    };
    this.reservationError = '';
    console.log(this.reserveAppointment);
    this.appointmentService.reserveAppointment(this.reserveAppointment).subscribe({
      next: () => {
        this.dialogRef?.close();
        this.snackBar.open('Cita agendada con éxito', 'Cerrar', {
          duration: 3000,
        });
      },
      error: () => {
        this.reservationError = 'No fue posible agendar la cita. Intenta nuevamente.';
      },
    });
  }
}
