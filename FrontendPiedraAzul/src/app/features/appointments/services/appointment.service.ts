import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';
import { PendingAppointment } from '../models/PendingAppointment';
import { getAppEnv } from '../../../core/config/app-env';

const env = getAppEnv();

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private appointmentsApi = env.API_APPOINTMENTS;
  private patientApi = env.API_PATIENT;
  constructor(private http: HttpClient) { }

  reserveAppointment(appointment: ReserveAppointmentDto): Observable<any> {
    return this.http.post<any>(`${this.appointmentsApi}/reserve`, appointment);
  }

  cancelAppointment(appointmentId: number): Observable<string> {
    return this.http.put(
      `${this.appointmentsApi}/cancel/${encodeURIComponent(appointmentId)}`,
      {},
      {
        responseType: 'text'
      }
    );
  }
  
  rescheduleAppointment(payload: ReserveAppointmentDto): Observable<string> {
    return this.http.post(`${this.appointmentsApi}/reschedulePatient`, payload, {
      responseType: 'text'
    });
  }

  getPendingAppointments(patientId: number): Observable<PendingAppointment[] | { pendingAppointments?: PendingAppointment[] }> {
    return this.http.get<PendingAppointment[] | { pendingAppointments?: PendingAppointment[] }>(
      `${this.patientApi}/${patientId}/getPendingAppointments`
    );
  }

}
