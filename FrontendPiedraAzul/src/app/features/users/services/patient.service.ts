import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getAppEnv } from '../../../core/config/app-env';
import { PatientProfile } from '../models/PatientProfile';
import { UpdatePatientRequest } from '../models/UpdatePatientRequest';

const env = getAppEnv();

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private usersApi = env.API_USERS;

  constructor(private http: HttpClient) {}

  getPatientById(patientId: number | string): Observable<PatientProfile> {
    return this.http.get<PatientProfile>(`${this.usersApi}/getPatientById/${patientId}`);
  }

  updatePatient(payload: UpdatePatientRequest): Observable<PatientProfile> {
    return this.http.put<PatientProfile>(`${this.usersApi}/updatePatient`, payload);
  }
}
