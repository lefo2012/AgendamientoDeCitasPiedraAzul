import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { AuthConfig, AUTH_CONFIG } from './auth.config';

export interface RegisterUserData {
  email: string;
  password: string;
  roles: string[];
}

export interface RegisterRequest {
  documentType: string;
  identificationNumber: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  phone: string;
  active: boolean;
  user: RegisterUserData;
}

export interface RegisterDoctorRequest extends RegisterRequest {
  specialties: string[];
  canSchedule: boolean;
}

export interface AuthTokenResponse {
  access_token: string;
  refresh_token?: string;
  expires_in?: number;
  refresh_expires_in?: number;
  token_type?: string;
  scope?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient,
    @Inject(AUTH_CONFIG) private config: AuthConfig
  ) {}

  private resolveAuthUrl(url: string): string {
    // In local development, Keycloak commonly runs on HTTP (localhost:8080).
    if (url.startsWith('https://localhost')) {
      const httpLocalUrl = url.replace('https://', 'http://');
      console.warn('[AuthService] Local HTTPS URL detected; using HTTP for local Keycloak dev:', httpLocalUrl);
      return httpLocalUrl;
    }

    return url;
  }

  register(data: RegisterRequest): Observable<any> {
    const url = this.resolveAuthUrl(`${this.config.backendApi}/registerPatient`);

    console.groupCollapsed('[AuthService] Register request');
    console.log('Endpoint:', url);
    console.log('Payload:', data);
    console.groupEnd();

    return this.http.post(url, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      withCredentials: false
    }).pipe(
      tap((response) => {
        console.groupCollapsed('[AuthService] Register response');
        console.log('Response payload:', response);
        console.groupEnd();
      }),
      catchError((error: HttpErrorResponse) => {
        console.groupCollapsed('[AuthService] Register error');
        console.error('HTTP status:', error.status);
        console.error('HTTP status text:', error.statusText);
        console.error('Error payload:', error.error);

        if (error.status === 0) {
          console.error('Network/CORS/SSL issue detected (status 0).');
        }

        console.groupEnd();
        return throwError(() => error);
      })
    );
  }

  registerDoctor(data: RegisterDoctorRequest): Observable<any> {
    const url = this.resolveAuthUrl(`${this.config.backendApi}/registerDoctor`);

    console.groupCollapsed('[AuthService] Register doctor request');
    console.log('Endpoint:', url);
    console.log('Payload:', data);
    console.groupEnd();

    return this.http.post(url, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      withCredentials: false
    }).pipe(
      tap((response) => {
        console.groupCollapsed('[AuthService] Register doctor response');
        console.log('Response payload:', response);
        console.groupEnd();
      }),
      catchError((error: HttpErrorResponse) => {
        console.groupCollapsed('[AuthService] Register doctor error');
        console.error('HTTP status:', error.status);
        console.error('HTTP status text:', error.statusText);
        console.error('Error payload:', error.error);

        if (error.status === 0) {
          console.error('Network/CORS/SSL issue detected (status 0).');
        }

        console.groupEnd();
        return throwError(() => error);
      })
    );
  }

  login(username: string, password: string): Observable<AuthTokenResponse> {
    const tokenUrl = this.resolveAuthUrl(this.config.keycloakTokenUrl);

    const body = new HttpParams()
      .set('client_id', this.config.clientId)
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password)
      .set('client_secret', 'HFn9D3q4cLaZyLfTcs7h4J4cDLLLaRLh');

    console.groupCollapsed('[AuthService] Keycloak login request');
    console.log('Endpoint:', tokenUrl);
    console.log('Client ID:', this.config.clientId);
    console.log('Username sent:', username);
    console.log('Grant type:', 'password');
    console.groupEnd();

    return this.http.post<AuthTokenResponse>(tokenUrl, body.toString(), {
      headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' }),
      withCredentials: false
    }).pipe(
      tap((response) => {
        console.groupCollapsed('[AuthService] Keycloak login response');
        console.log('Full response object:', response);
        console.log('access_token:', response?.access_token ?? '(none)');
        console.log('token_type:', response?.token_type ?? '(none)');
        console.log('expires_in:', response?.expires_in ?? '(none)');
        console.groupEnd();
      }),
      catchError((error: HttpErrorResponse) => {
        console.groupCollapsed('[AuthService] Keycloak login error');
        console.error('HTTP status:', error.status);
        console.error('HTTP status text:', error.statusText);
        console.error('Error payload:', error.error);

        if (error.status === 400 && error.error?.error === 'invalid_grant') {
          console.error('Detected invalid credentials (invalid_grant).');
        } else if (error.status === 0) {
          console.error('Network/CORS/SSL issue detected (status 0).');
        }

        console.groupEnd();
        return throwError(() => error);
      })
    );
  }

  logout(): Observable<any> {
    const url = this.resolveAuthUrl(`${this.config.backendApi}/logout`);
    return this.http.post(url, {}, { withCredentials: true });
  }

getRolesFromToken(token: string): string[] {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload?.realm_access?.roles || [];
  } catch (e) {
    console.error('Error parsing token', e);
    return [];
  }
}





}