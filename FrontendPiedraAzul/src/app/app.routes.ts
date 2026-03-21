import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { RegisterUser } from './features/users/pages/register-user/register-user';
import { Login } from './features/users/pages/login-user/login-user';
import { Header } from './layout/header/header';
import { Main } from './features/main/pages/main/main';
import { About } from './features/main/pages/about/about';
import { CreateAppointment } from './features/appointments/pages/create-appointment/create-appointment';
import { RescheduleAppointment } from './features/appointments/pages/reschedule-appointment/reschedule-appointment';
import { CancelAppointment } from './features/appointments/pages/cancel-appointment/cancel-appointment';
import { AdminHome } from './features/users/pages/admin-home/admin-home';
export const routes: Routes = [

  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: '',
        component: Main

      },
      
      {
        path: 'about',
        component: About
      },
      {
        path: 'register',
        component: RegisterUser
      },
      {
        path: 'login',
        component: Login
      },
      {
        path: 'admin',
        component: AdminHome
      },
      
      {
        path: 'citas',
        redirectTo: 'citas/agendar',
        pathMatch: 'full'
      },
      {
        path: 'citas/agendar',
        component: CreateAppointment
      },
      {
        path: 'citas/reagendar',
        component: RescheduleAppointment
      },
      {
        path: 'citas/cancelar',
        component: CancelAppointment
      },
      {
        path:'header',
        component: Header
      },
      

    ]

  }

];