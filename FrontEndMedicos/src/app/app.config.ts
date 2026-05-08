import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { AUTH_CONFIG, defaultAuthConfig } from './core/auth/auth.config';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideNativeDateAdapter } from '@angular/material/core';
import { authInterceptor } from './core/auth/auth.interceptor';

export const appConfig: ApplicationConfig = {
	providers: [
		provideBrowserGlobalErrorListeners(),
		provideRouter(routes),
		provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
		provideAnimations(),
		provideClientHydration(withEventReplay()),
		provideNativeDateAdapter(),
		{ provide: AUTH_CONFIG, useValue: defaultAuthConfig }
	]
};
