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
        path: 'about',
        loadComponent: () =>
          import('./features/main/pages/about/about').then((m) => m.About),
      },
      {
        path: 'admin',
        loadComponent: () =>
          import('./features/users/pages/admin-home/admin-home').then((m) => m.AdminHome),
      },
      {
        path: 'admin/registrar-medico',
        loadComponent: () =>
          import('./features/users/pages/register-doctor/register-doctor').then((m) => m.RegisterDoctor),
      },
      {
        path: 'citas',
        redirectTo: 'citas/agendar',
        pathMatch: 'full',
      },
      {
        path: 'citas/agendar',
        loadComponent: () =>
          import('./features/appointments/pages/create-appointment/create-appointment').then((m) => m.CreateAppointment),
      },
      {
        path: 'citas/reagendar',
        loadComponent: () =>
          import('./features/appointments/pages/reschedule-appointment/reschedule-appointment').then((m) => m.RescheduleAppointment),
      },
      {
        path: 'citas/cancelar',
        loadComponent: () =>
          import('./features/appointments/pages/cancel-appointment/cancel-appointment').then((m) => m.CancelAppointment),
      },
      {
        path: 'header',
        loadComponent: () =>
          import('./layout/header/header').then((m) => m.Header),
      },
    ],
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/auth-layout/auth-layout').then((m) => m.AuthLayout),
    children: [
      {
        path: 'register',
        loadComponent: () =>
          import('./features/users/pages/register-user/register-user').then((m) => m.RegisterUser),
      },
      {
        path: 'login',
        loadComponent: () =>
          import('./features/users/pages/login-user/login-user').then((m) => m.Login),
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];