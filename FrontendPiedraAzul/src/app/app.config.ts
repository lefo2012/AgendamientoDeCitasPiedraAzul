import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { AUTH_CONFIG, defaultAuthConfig } from './features/users/services/auth.config';

import { provideNativeDateAdapter } from '@angular/material/core';
import { provideAnimations } from '@angular/platform-browser/animations';
import { authInterceptor } from './features/users/services/auth.interceptor';

export const appConfig: ApplicationConfig = {
	providers: [
		provideBrowserGlobalErrorListeners(),
		provideRouter(routes),
		provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
		provideClientHydration(withEventReplay()),
		provideAnimations(),
		provideNativeDateAdapter(),
		{ provide: AUTH_CONFIG, useValue: defaultAuthConfig }
	]
};