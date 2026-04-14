import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorSchedule } from '../models/DoctorSchedule';
import { DoctorDto } from '../models/DoctorDto';
import { PatientDto } from '../models/PatientDto';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';
import { ScheduleSlot } from '../models/ScheduleSlot';
import { AuthService } from '../../users/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private apiUrl = '/api/doctor';
  private usersApi = '/api/users';
  private appointmentsApi = '/api/appointments';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  generateScheduleSlots(
    daysOfWeek: number[],
    startHour: number,
    endHour: number,
    intervalMinutes: number,
    weekStart: Date,
    weekEnd: Date,
    startMinute: number = 0,
    endMinute: number = 0
  ): ScheduleSlot[] {
    const slots: ScheduleSlot[] = [];

    let currentDate = new Date(weekStart);
    while (currentDate <= weekEnd) {
      const dayOfWeek = currentDate.getDay();

      if (daysOfWeek.includes(dayOfWeek)) {
        let hours = startHour;
        let minutes = startMinute;

        while (hours < endHour || (hours === endHour && minutes < endMinute)) {
          const timeString = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;

          slots.push({
            time: timeString,
            dayOfWeek: dayOfWeek,
            date: new Date(currentDate)
          });

          minutes += intervalMinutes;
          if (minutes >= 60) {
            hours += Math.floor(minutes / 60);
            minutes = minutes % 60;
          }

          if (hours > endHour) {
            break;
          }
        }
      }

      currentDate.setDate(currentDate.getDate() + 1);
    }

    return slots;
  }

  getDayName(dayOfWeek: number): string {
    const days = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
    return days[dayOfWeek];
  }

  private buildAuthHeaders(): HttpHeaders {
    const accessToken = this.authService.accessToken();

    return accessToken
      ? new HttpHeaders({ Authorization: `Bearer ${accessToken}` })
      : new HttpHeaders();
  }

  saveDoctorSchedule(doctorId: string, schedule: DoctorSchedule): Observable<string> {
    return this.http.post(`${this.apiUrl}/${doctorId}/configureSchedule`, schedule, {
      headers: this.buildAuthHeaders(),
      responseType: 'text'
    });
  }

  getDoctorSchedule(doctorId: string): Observable<DoctorSchedule> {
    return this.http.get<DoctorSchedule>(`${this.apiUrl}/${doctorId}/schedule`, {
      headers: this.buildAuthHeaders()
    });
  }

  getAllDoctors(): Observable<DoctorDto[]> {
    return this.http.get<DoctorDto[]>(`${this.usersApi}/getAllDoctors`, {
      headers: this.buildAuthHeaders()
    });
  }

  getPatientByIdentificationNumber(identificationNumber: string): Observable<PatientDto> {
    return this.http.get<PatientDto>(
      `${this.usersApi}/getPatientByIdentificationNumber/${encodeURIComponent(identificationNumber)}`
    );
  }

  searchPatientsByIdentificationNumber(identificationNumber: string): Observable<PatientDto[]> {
    return this.http.get<PatientDto[]>(
      `${this.usersApi}/searchPatientsByIdentificationNumber/${encodeURIComponent(identificationNumber)}`
      , {
        headers: this.buildAuthHeaders()
      }
    );
  }

  reserveAppointment(payload: ReserveAppointmentDto): Observable<string> {
    return this.http.post(`${this.appointmentsApi}/reserve`, payload, {
      headers: this.buildAuthHeaders(),
      responseType: 'text',
    });
  }
}
