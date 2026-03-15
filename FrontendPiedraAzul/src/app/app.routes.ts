import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { AppointmentList } from './features/appointments/pages/appointment-list/appointment-list';
import { RegisterUser } from './features/users/pages/register-user/register-user';
import { Login } from './features/users/pages/login-user/login-user';

export const routes: Routes = [

  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: '',
        component: AppointmentList
      
      },
      {
        path: 'register',
        component: RegisterUser
      },
       {
        path: 'login',
        component: Login
  }

    ]
    


  }
  

];