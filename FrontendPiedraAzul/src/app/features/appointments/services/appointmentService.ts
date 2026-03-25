import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DoctorDto } from '../models/DoctorDto';
import { HttpClient } from '@angular/common/http';
import { ReserveAppointmentDto } from '../models/ReserveAppointmentDto';

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private apiUrl = '/api/doctor';
  private usersApi = '/api/users';
  private appointmentsApi = '/api/appointments';
  constructor(private http: HttpClient) { }
  
  getDoctorsBySpeciality(specialty: string): Observable<DoctorDto[]> {
    return this.http.get<DoctorDto[]>(`${this.usersApi}/getDoctorsBySpecialty/${specialty}`);
    console.log(specialty);
  }
  reserveAppointment(appointment: ReserveAppointmentDto): Observable<any> {
    console.log(appointment);
    return this.http.post<any>(`${this.appointmentsApi}/reserve`, appointment);
  }

}
