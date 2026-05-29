import { InjectionToken } from '@angular/core';
import { getAppEnv } from '../config/app-env';

export interface AuthConfig {
  backendApi: string;
  authApi: string;
  keycloakTokenUrl: string;
  clientId: string;
  clientSecret: string;
}

export const AUTH_CONFIG = new InjectionToken<AuthConfig>('auth.config');

const env = getAppEnv();

export const defaultAuthConfig: AuthConfig = {
  backendApi: env.BACKEND_API,
  authApi: env.API_AUTH,
  keycloakTokenUrl: env.KEYCLOAK_TOKEN_URL,
  clientId: env.KEYCLOAK_CLIENT_ID,
  clientSecret: env.KEYCLOAK_CLIENT_SECRET
};