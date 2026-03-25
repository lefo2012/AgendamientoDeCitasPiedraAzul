import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch } from '@angular/common/http';

import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { AUTH_CONFIG, defaultAuthConfig } from './features/users/services/auth.config';

import { provideNativeDateAdapter } from '@angular/material/core';
import { provideAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
	providers: [
		provideBrowserGlobalErrorListeners(),
		provideRouter(routes),
		provideHttpClient(withFetch()),
		provideClientHydration(withEventReplay()),
		provideAnimations(),
		provideNativeDateAdapter(),
		{ provide: AUTH_CONFIG, useValue: defaultAuthConfig }
	]
};
