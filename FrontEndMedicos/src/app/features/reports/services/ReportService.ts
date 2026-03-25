import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppointmentReportDto } from '../models/AppointmentReportDto';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = '/api/reports';

  constructor(private http: HttpClient) {}

  getAppointmentsByDoctorAndDate(
    doctorId: number,
    appointmentDate: string
  ): Observable<AppointmentReportDto[]> {
    return this.http.get<AppointmentReportDto[]>(
      `${this.apiUrl}/appointmentsByDoctorAndDate`,
      {
        params: {
          doctorId: doctorId,
          appointmentDate: appointmentDate
        }
      }
    );
  }

  getAllAppointments(): Observable<AppointmentReportDto[]> {
    return this.http.get<AppointmentReportDto[]>(`${this.apiUrl}/appointmentsReport`);
  }
}