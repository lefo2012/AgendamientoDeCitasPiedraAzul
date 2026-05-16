import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';
import { getAppEnv } from '../../../core/config/app-env';

const env = getAppEnv();

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private appointmentsApi = env.API_APPOINTMENTS;
  constructor(private http: HttpClient) { }
  reserveAppointment(appointment: ReserveAppointmentDto): Observable<any> {
    return this.http.post<any>(`${this.appointmentsApi}/reserve`, appointment);
  }

}
