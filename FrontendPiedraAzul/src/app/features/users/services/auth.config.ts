import { InjectionToken } from '@angular/core';

export interface AuthConfig {
  backendApi: string;
  keycloakTokenUrl: string;
  clientId: string;
}

export const AUTH_CONFIG = new InjectionToken<AuthConfig>('auth.config');

export const defaultAuthConfig: AuthConfig = {
  backendApi: 'http://localhost:8081/auth',
  keycloakTokenUrl: 'http://localhost:8080/realms/piedraAzul-dev/protocol/openid-connect/token',
  clientId: 'piedraAzul-app'
};
