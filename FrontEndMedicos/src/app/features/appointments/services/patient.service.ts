import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PatientDto } from '../models/PatientDto';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private usersApi = '/api/users';

  constructor(private http: HttpClient) {}

  getPatientByIdentificationNumber(identificationNumber: string): Observable<PatientDto> {
    return this.http.get<PatientDto>(
      `${this.usersApi}/getPatientByIdentificationNumber/${encodeURIComponent(identificationNumber)}`
    );
  }

  searchPatientsByIdentificationNumber(identificationNumber: string): Observable<PatientDto[]> {
    return this.http.get<PatientDto[]>(
      `${this.usersApi}/searchPatientsByIdentificationNumber/${encodeURIComponent(identificationNumber)}`
    );
  }
}
