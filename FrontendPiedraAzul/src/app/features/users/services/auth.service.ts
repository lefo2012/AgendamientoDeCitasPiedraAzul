import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthConfig, AUTH_CONFIG } from './auth.config';

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  role: string;
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

  private ensureSecureUrl(url: string): string {
    if (url.startsWith('http://')) {
      console.warn('Insecure auth URL requested, updating to HTTPS for transport encryption.');
      return url.replace('http://', 'https://');
    }
    return url;
  }

  register(data: RegisterRequest): Observable<any> {
    const url = this.ensureSecureUrl(`${this.config.backendApi}/register`);
    return this.http.post(url, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      withCredentials: true
    });
  }

  login(username: string, password: string): Observable<AuthTokenResponse> {
    const tokenUrl = this.ensureSecureUrl(this.config.keycloakTokenUrl);

    const body = new HttpParams()
      .set('client_id', this.config.clientId)
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password);

    return this.http.post<AuthTokenResponse>(tokenUrl, body.toString(), {
      headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' }),
      withCredentials: true
    });
  }

  logout(): Observable<any> {
    const url = this.ensureSecureUrl(`${this.config.backendApi}/logout`);
    return this.http.post(url, {}, { withCredentials: true });
  }
}
