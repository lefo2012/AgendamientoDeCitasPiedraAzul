import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DoctorDto } from '../models/DoctorDto';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private usersApi = '/api/users';

  constructor(private http: HttpClient) {}

  getDoctorsBySpeciality(specialty: string): Observable<DoctorDto[]> {
    return this.http.get<DoctorDto[]>(`${this.usersApi}/getDoctorsBySpecialty/${specialty}`);
  }
}
