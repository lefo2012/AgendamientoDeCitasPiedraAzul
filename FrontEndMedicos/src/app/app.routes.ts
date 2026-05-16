import { Routes } from '@angular/router';
import { authRequiredGuard } from './core/guards/auth-required.guard';

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
				canActivate: [authRequiredGuard],
				loadComponent: () =>
					import('./features/reports/pages/appointment-table/appointment-table').then((m) => m.AppointmentTable),
			},
			{
				path: 'citas/nueva',
				loadComponent: () =>
					import('./features/appointments/pages/create-appointment/create-appointment').then((m) => m.CreateAppointment),
			},
			{
				path: 'admin',
				loadComponent: () =>
					import('./features/users/pages/admin-home/admin-home').then((m) => m.AdminHome),
			},
			{
				path: 'admin/registrar-medico',
				loadComponent: () =>
					import('./features/users/pages/register-user/register-user').then((m) => m.RegisterUser),
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
					import('./features/users/pages/register-user/register-user').then((m) => m.RegisterUser),
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
