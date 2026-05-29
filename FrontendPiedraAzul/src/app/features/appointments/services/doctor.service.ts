import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorDto } from '../models/DoctorDto';
import { getAppEnv } from '../../../core/config/app-env';
import { AppointmentDoctorDto } from '../../users/models/AppointmentDoctorDto';

const env = getAppEnv();

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private usersApi = env.API_USERS;

  constructor(private http: HttpClient) {}

  getDoctorsBySpeciality(specialty: string): Observable<DoctorDto[]> {
    return this.http.get<DoctorDto[]>(`${this.usersApi}/getDoctorsBySpecialty/${specialty}`);
  }

  getDoctorById(doctorId: number): Observable<AppointmentDoctorDto> {
    return this.http.get<AppointmentDoctorDto>(`${this.usersApi}/getDoctorById/${doctorId}`);
  }
}
