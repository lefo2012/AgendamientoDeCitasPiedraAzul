import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200', // cambiar a 4300 en el de pacientes    
    chromeWebSecurity: false, // necesario para Keycloak
  }
});