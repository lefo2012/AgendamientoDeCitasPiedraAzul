import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorSchedule } from '../models/DoctorSchedule';
import { DoctorDto } from '../models/DoctorDto';
import { PatientDto } from '../models/PatientDto';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';
import { ScheduleSlot } from '../models/ScheduleSlot';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private apiUrl = '/api/doctor';
  private usersApi = '/api/users';
  private appointmentsApi = '/api/appointments';

  constructor(private http: HttpClient) {}

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

  saveDoctorSchedule(doctorId: string, schedule: DoctorSchedule): Observable<string> {
    return this.http.post(`${this.apiUrl}/${doctorId}/configureSchedule`, schedule, {
      responseType: 'text'
    });
  }

  getDoctorSchedule(doctorId: string): Observable<DoctorSchedule> {
    return this.http.get<DoctorSchedule>(`${this.apiUrl}/${doctorId}/schedule`);
  }

  getAllDoctors(): Observable<DoctorDto[]> {
    return this.http.get<DoctorDto[]>(`${this.usersApi}/getAllDoctors`);
  }

  getPatientByIdentificationNumber(identificationNumber: string): Observable<PatientDto> {
    return this.http.get<PatientDto>(
      `${this.usersApi}/getPatientByIdentificationNumber/${encodeURIComponent(identificationNumber)}`
    );
  }

  searchPatientsByIdentificationNumber(identificationNumber: string): Observable<PatientDto[]> {
    return this.http.get<PatientDto[]>(
      `${this.usersApi}/searchPatientsByIdentificationNumber/${encodeURIComponent(identificationNumber)}`
    );
  }

  reserveAppointment(payload: ReserveAppointmentDto): Observable<string> {
    return this.http.post(`${this.appointmentsApi}/reserve`, payload, {
      responseType: 'text',
    });
  }
}
