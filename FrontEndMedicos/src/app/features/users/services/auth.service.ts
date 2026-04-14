import { computed, Inject, Injectable, PLATFORM_ID, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, catchError, finalize, map, of, shareReplay, switchMap, tap, throwError } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { AuthConfig, AUTH_CONFIG } from './auth.config';
import { AuthTokenResponse } from '../models/AuthTokenResponse';
import { RegisterDoctorRequest } from '../models/RegisterDoctorRequest';
import { RegisterRequest } from '../models/RegisterRequest';

interface CurrentPatient {
  id?: string | number;
  email?: string;
  firstName?: string;
  lastName?: string;
  [key: string]: unknown;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly accessTokenKey = 'piedraAzul_access_token';
  private readonly refreshTokenKey = 'piedraAzul_refresh_token';
  private readonly currentPatientKey = 'piedraAzul_current_patient';
  private readonly accessTokenSignal = signal<string | null>(this.readStoredAccessToken());
  private readonly refreshTokenSignal = signal<string | null>(this.readStoredRefreshToken());
  private readonly currentPatientSignal = signal<CurrentPatient | null>(this.readStoredCurrentPatient());
  private refreshInFlight$: Observable<string> | null = null;

  readonly accessToken = this.accessTokenSignal.asReadonly();
  readonly refreshToken = this.refreshTokenSignal.asReadonly();
  readonly currentPatient = this.currentPatientSignal.asReadonly();
  readonly isAuthenticated = computed(() => !!this.accessTokenSignal());

  constructor(
    private http: HttpClient,
    @Inject(AUTH_CONFIG) private config: AuthConfig,
    @Inject(PLATFORM_ID) private platformId: object
  ) {}

  private get isBrowser(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  private readStoredAccessToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }

    return localStorage.getItem(this.accessTokenKey);
  }

  private readStoredRefreshToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }

    return localStorage.getItem(this.refreshTokenKey);
  }

  private readStoredCurrentPatient(): CurrentPatient | null {
    if (!this.isBrowser) {
      return null;
    }

    const rawValue = localStorage.getItem(this.currentPatientKey);

    if (!rawValue) {
      return null;
    }

    try {
      return JSON.parse(rawValue) as CurrentPatient;
    } catch (error) {
      console.warn('[AuthService] Failed to parse stored current patient. Clearing invalid cache.', error);
      localStorage.removeItem(this.currentPatientKey);
      return null;
    }
  }

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

  logout(): Observable<void> {
    this.clearSession();
    return of(void 0);
  }

  startSessionWithToken(tokenResponse: AuthTokenResponse): void {
    this.storeTokens(tokenResponse);
  }

  initializeSession(tokenResponse: AuthTokenResponse): Observable<CurrentPatient> {
    this.storeTokens(tokenResponse);
    const accessToken = tokenResponse.access_token;

    if (!accessToken) {
      return throwError(() => new Error('No access token available to initialize session.'));
    }

    return this.fetchCurrentPatient(accessToken).pipe(
      tap((patient) => {
        this.currentPatientSignal.set(patient);

        if (this.isBrowser) {
          localStorage.setItem(this.currentPatientKey, JSON.stringify(patient));
        }
      })
    );
  }

  restoreSession(): Observable<CurrentPatient | null> {
    const accessToken = this.readStoredAccessToken();

    if (!accessToken) {
      this.clearSession();
      return of(null);
    }

    this.accessTokenSignal.set(accessToken);
    this.refreshTokenSignal.set(this.readStoredRefreshToken());

    const roles = this.getRolesFromToken(accessToken);
    if (roles.includes('admin')) {
      return of(this.readStoredCurrentPatient());
    }

    const cachedPatient = this.readStoredCurrentPatient();

    if (cachedPatient) {
      this.currentPatientSignal.set(cachedPatient);
      return of(cachedPatient);
    }

    return this.fetchCurrentPatient(accessToken).pipe(
      tap((patient) => {
        this.currentPatientSignal.set(patient);

        if (this.isBrowser) {
          localStorage.setItem(this.currentPatientKey, JSON.stringify(patient));
        }
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          return this.refreshAccessToken().pipe(
            switchMap((newAccessToken) => this.fetchCurrentPatient(newAccessToken)),
            tap((patient) => {
              this.currentPatientSignal.set(patient);

              if (this.isBrowser) {
                localStorage.setItem(this.currentPatientKey, JSON.stringify(patient));
              }
            }),
            catchError((refreshError: HttpErrorResponse) => {
              this.clearSession();
              return throwError(() => refreshError);
            })
          );
        }

        this.clearSession();
        return throwError(() => error);
      })
    );
  }

  refreshAccessToken(): Observable<string> {
    const refreshToken = this.refreshTokenSignal() ?? this.readStoredRefreshToken();

    if (!refreshToken) {
      return throwError(() => new Error('No refresh token available.'));
    }

    if (this.refreshInFlight$) {
      return this.refreshInFlight$;
    }

    const tokenUrl = this.resolveAuthUrl(this.config.keycloakTokenUrl);
    const body = new HttpParams()
      .set('client_id', this.config.clientId)
      .set('grant_type', 'refresh_token')
      .set('refresh_token', refreshToken)
      .set('client_secret', 'HFn9D3q4cLaZyLfTcs7h4J4cDLLLaRLh');

    this.refreshInFlight$ = this.http.post<AuthTokenResponse>(tokenUrl, body.toString(), {
      headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' }),
      withCredentials: false
    }).pipe(
      tap((tokenResponse) => {
        console.groupCollapsed('[AuthService] Refresh token response');
        console.log('access_token:', tokenResponse?.access_token ?? '(none)');
        console.log('refresh_token:', tokenResponse?.refresh_token ? '(received)' : '(not provided)');
        console.groupEnd();

        this.storeTokens(tokenResponse);
      }),
      map((tokenResponse) => tokenResponse.access_token),
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
    this.accessTokenSignal.set(null);
    this.refreshTokenSignal.set(null);
    this.currentPatientSignal.set(null);
    this.refreshInFlight$ = null;

    if (this.isBrowser) {
      localStorage.removeItem(this.accessTokenKey);
      localStorage.removeItem(this.refreshTokenKey);
      localStorage.removeItem(this.currentPatientKey);
    }
  }

  private storeTokens(tokenResponse: AuthTokenResponse): void {
    this.accessTokenSignal.set(tokenResponse.access_token);

    if (tokenResponse.refresh_token) {
      this.refreshTokenSignal.set(tokenResponse.refresh_token);
    }

    if (!this.isBrowser) {
      return;
    }

    localStorage.setItem(this.accessTokenKey, tokenResponse.access_token);

    if (tokenResponse.refresh_token) {
      localStorage.setItem(this.refreshTokenKey, tokenResponse.refresh_token);
    }
  }

  private fetchCurrentPatient(accessToken: string): Observable<CurrentPatient> {
    return this.http.get<CurrentPatient>('/api/auth/getDoctorByToken', {
      headers: new HttpHeaders({
        Authorization: `Bearer ${accessToken}`
      })
    }).pipe(
      tap((patient) => {
        console.groupCollapsed('[AuthService] /api/auth/getDoctorByToken response');
        console.log('Current patient payload:', patient);
        console.groupEnd();
      })
    );
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