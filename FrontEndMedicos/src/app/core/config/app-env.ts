export interface AppEnv {
  BACKEND_API: string;
  API_USERS: string;
  API_APPOINTMENTS: string;
  API_DOCTOR: string;
  API_REPORTS: string;
  API_AUTH: string;
  KEYCLOAK_TOKEN_URL: string;
  KEYCLOAK_CLIENT_ID: string;
  KEYCLOAK_CLIENT_SECRET: string;
}

declare global {
  interface Window {
    __env?: Partial<AppEnv>;
  }
}

const defaultEnv: AppEnv = {
  BACKEND_API: '/api/users',
  API_USERS: '/api/users',
  API_APPOINTMENTS: '/api/appointments',
  API_DOCTOR: '/api/doctor',
  API_REPORTS: '/api/reports',
  API_AUTH: '/api/auth',
  KEYCLOAK_TOKEN_URL: 'http://localhost:8080/realms/piedraAzul-dev/protocol/openid-connect/token',
  KEYCLOAK_CLIENT_ID: 'piedraAzul-app',
  KEYCLOAK_CLIENT_SECRET: ''
};

export const getAppEnv = (): AppEnv => {
  if (typeof window === 'undefined') {
    return { ...defaultEnv };
  }

  const runtimeEnv = window.__env || {};

  return {
    ...defaultEnv,
    ...runtimeEnv
  };
};
