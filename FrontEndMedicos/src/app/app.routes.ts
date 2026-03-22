import { Routes } from '@angular/router';

export const routes: Routes = [
	{
		path: '',
		loadComponent: () =>
			import('./layout/main-layout/main-layout').then((m) => m.MainLayout),
		children: [
			{
				path: '',
				loadComponent: () =>
					import('./features/main/pages/main/main').then((m) => m.Main),
			},
			{
				path: 'citas',
				redirectTo: 'citas/agendar',
				pathMatch: 'full',
			},
			{
				path: 'citas/agendar',
				loadComponent: () =>
					import('./features/appointments/pages/appointment-table/appointment-table').then((m) => m.AppointmentTable),
			},
		],
	},
	{
		path: '',
		loadComponent: () =>
			import('./layout/auth-layout/auth-layout').then((m) => m.AuthLayout),
		children: [
			{
				path: 'login',
				loadComponent: () =>
					import('./features/users/pages/login-user/login-user').then((m) => m.Login),
			},
			{
				path: 'register',
				loadComponent: () =>
					import('./features/users/pages/register-user/register-user').then((m) => m.RegisterDoctor),
			},
			{
				path: 'configure-schedule',
				loadComponent: () =>
					import('./features/users/pages/configure-schedule-doctor/configure-schedule-doctor').then((m) => m.ConfigureScheduleDoctor),
			},
		],
	},
	{
		path: '**',
		redirectTo: '',
	},
];
