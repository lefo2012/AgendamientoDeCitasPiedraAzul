import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DoctorDto } from '../models/DoctorDto';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private apiUrl = '/api/doctor';
  private usersApi = '/api/users';
  private appointmentsApi = '/api/appointments';
  constructor(private http: HttpClient) { }
  
  getDoctorsBySpeciality(speciality: string): Observable<DoctorDto[]> {
    return this.http.get<DoctorDto[]>(`${this.usersApi}/getDoctorsBySpecialty/${speciality}`);
    console.log(speciality);
  }

}
