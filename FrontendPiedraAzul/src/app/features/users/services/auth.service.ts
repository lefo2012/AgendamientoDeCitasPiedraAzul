import { computed, Inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, finalize, map, of, shareReplay, switchMap, tap, throwError } from 'rxjs';
import { AuthConfig, AUTH_CONFIG } from '../../../core/auth/auth.config';
import { RegisterDoctorRequest } from '../models/RegisterDoctorRequest';
import { RegisterRequest } from '../models/RegisterRequest';
import { CurrentPatient } from '../models/CurrentPatient';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private static readonly SESSION_CLIENT = 'patient';

  private readonly currentPatientSignal = signal<CurrentPatient | null>(null);
  private readonly rolesSignal = signal<string[]>([]);
  private readonly sessionActiveSignal = signal<boolean>(false);
  private refreshInFlight$: Observable<void> | null = null;

  readonly currentPatient = this.currentPatientSignal.asReadonly();
  readonly roles = this.rolesSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.sessionActiveSignal());

  getSessionClient(): string {
    return AuthService.SESSION_CLIENT;
  }

  constructor(
    private http: HttpClient,
    @Inject(AUTH_CONFIG) private config: AuthConfig
  ) {}

  private resolveAuthUrl(url: string): string {
    
    if (url.startsWith('https://localhost')) {
      const httpLocalUrl = url.replace('https://', 'http://');
      console.warn('[AuthService] Local HTTPS URL detected; using HTTP for local Keycloak dev:', httpLocalUrl);
      return httpLocalUrl;
    }

    return url;
  }

  register(data: RegisterRequest): Observable<any> {
    const url = this.resolveAuthUrl(`${this.config.backendApi}/registerPatient`);

    return this.http.post(url, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      withCredentials: false
    }).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 0) {
          console.error('Network/CORS/SSL issue detected (status 0).');
        }
        return throwError(() => error);
      })
    );
  }

  registerDoctor(data: RegisterDoctorRequest): Observable<any> {
    const url = this.resolveAuthUrl(`${this.config.backendApi}/registerDoctor`);

    return this.http.post(url, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      withCredentials: false
    }).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 0) {
          console.error('Network/CORS/SSL issue detected (status 0).');
        }
        return throwError(() => error);
      })
    );
  }

  login(username: string, password: string): Observable<void> {
    const url = `${this.config.authApi}/session/login`;

    return this.http.post<void>(url, { username, password }, {
      headers: this.buildSessionHeaders(),
      withCredentials: true
    }).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.error?.error === 'invalid_credentials') {
          console.error('Detected invalid credentials.');
        } else if (error.status === 0) {
          console.error('Network/CORS issue detected (status 0).');
        }
        return throwError(() => error);
      })
    );
  }

  logout(): Observable<void> {
    const url = `${this.config.authApi}/session/logout`;

    return this.http.post<void>(url, {}, {
      headers: this.buildSessionHeaders(),
      withCredentials: true
    }).pipe(
      tap(() => this.clearSession()),
      catchError((error: HttpErrorResponse) => {
        this.clearSession();
        return throwError(() => error);
      })
    );
  }

  initializeSession(): Observable<CurrentPatient | null> {
    return this.fetchCurrentPatient().pipe(
      switchMap((patient) => this.loadRoles().pipe(
        map(() => patient),
        catchError(() => of(patient))
      )),
      tap((patient) => {
        this.currentPatientSignal.set(patient);
        this.sessionActiveSignal.set(true);
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 400) {
          this.clearSession();
          return of(null);
        }

        this.clearSession();
        return throwError(() => error);
      })
    );
  }

  restoreSession(): Observable<CurrentPatient | null> {
    return this.initializeSession();
  }

  refreshAccessToken(): Observable<void> {
    if (this.refreshInFlight$) {
      return this.refreshInFlight$.pipe(map(() => void 0));
    }

    const url = `${this.config.authApi}/session/refresh`;

    this.refreshInFlight$ = this.http.post<void>(url, {}, {
      headers: this.buildSessionHeaders(),
      withCredentials: true
    }).pipe(
      map(() => void 0),
      catchError((error: HttpErrorResponse) => {
        this.clearSession();
        return throwError(() => error);
      }),
      finalize(() => {
        this.refreshInFlight$ = null;
      }),
      shareReplay(1)
    );

    return this.refreshInFlight$;
  }

  clearSession(): void {
    this.currentPatientSignal.set(null);
    this.rolesSignal.set([]);
    this.sessionActiveSignal.set(false);
    this.refreshInFlight$ = null;
  }

  private fetchCurrentPatient(): Observable<CurrentPatient> {
    return this.http.get<CurrentPatient>(`${this.config.authApi}/getPatientByToken`, {
      headers: this.buildSessionHeaders(),
      withCredentials: true
    });
  }

  private buildSessionHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Auth-Client': AuthService.SESSION_CLIENT
    });
  }

  private loadRoles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.config.authApi}/roles`, {
      withCredentials: true
    }).pipe(
      tap((roles) => this.rolesSignal.set(roles ?? []))
    );
  }

  getRoles(): string[] {
    return this.rolesSignal();
  }
}