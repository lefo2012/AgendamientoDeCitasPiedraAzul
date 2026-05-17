import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';
import { getAppEnv } from '../../../core/config/app-env';

const env = getAppEnv();

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private appointmentsApi = env.API_APPOINTMENTS;

  constructor(private http: HttpClient) { }

  reserveAppointment(payload: ReserveAppointmentDto): Observable<string> {
    return this.http.post(`${this.appointmentsApi}/reserve`, payload, {
      responseType: 'text'
    });
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
}
