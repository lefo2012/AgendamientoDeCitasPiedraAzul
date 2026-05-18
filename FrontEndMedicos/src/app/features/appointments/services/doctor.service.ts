import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorSchedule } from '../models/DoctorSchedule';
import { DoctorDto } from '../models/DoctorDto';
import { getAppEnv } from '../../../core/config/app-env';
import { ConfigDoctor } from '../../users/models/ConfigDoctor';
import { RegisterDoctorRequest } from '../../users/models/RegisterDoctorRequest';
import { UpdateDoctorDto } from '../../users/models/UpdateDoctorDto';

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

  getAllConfigurableDoctors(): Observable<ConfigDoctor[]> {
    return this.http.get<ConfigDoctor[]>(`${this.usersApi}/getAllConfigDoctors`);
  }

  getDoctorById(doctorId: number): Observable<UpdateDoctorDto> {
    return this.http.get<UpdateDoctorDto>(`${this.usersApi}/getDoctorById/${doctorId}`);
  }

  updateDoctor(payload: UpdateDoctorDto): Observable<string> {
    return this.http.put(`${this.usersApi}/updateDoctor`, payload, {
      responseType: 'text'
    });
  }

}
