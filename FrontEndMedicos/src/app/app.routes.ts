import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { Main } from './features/main/pages/main/main';
import { Login } from './features/users/pages/login-user/login-user';
import { RegisterDoctor} from './features/users/pages/register-user/register-user';
import { AppointmentTable } from './features/appointments/pages/appointment-table/appointment-table';

export const routes: Routes = [
	{
		path: '',
		component: MainLayout,
		children: [
			{
				path: '',
				component: Main,
			},
			{
				path: 'login',
				component: Login,
			},
			{
				path: 'register',
				component: RegisterDoctor,
			},
			{
				path: 'citas',
				redirectTo: 'citas/agendar',
				pathMatch: 'full',
			},
			{
				path: 'citas/agendar',
				component: AppointmentTable,
			},
		],
	},
	{
		path: '**',
		redirectTo: '',
	},
];
