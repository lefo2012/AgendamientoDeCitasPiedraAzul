import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private appointmentsApi = '/api/appointments';

  constructor(private http: HttpClient) {}

  reserveAppointment(payload: ReserveAppointmentDto): Observable<string> {
    return this.http.post(`${this.appointmentsApi}/reserve`, payload, {
      responseType: 'text'
    });
  }
}
