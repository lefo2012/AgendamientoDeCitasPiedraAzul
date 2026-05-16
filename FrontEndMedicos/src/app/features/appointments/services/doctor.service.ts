import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorSchedule } from '../models/DoctorSchedule';
import { DoctorDto } from '../models/DoctorDto';
import { getAppEnv } from '../../../core/config/app-env';

const env = getAppEnv();

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private apiUrl = env.API_DOCTOR;
  private usersApi = env.API_USERS;

  constructor(private http: HttpClient) {}

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
}
