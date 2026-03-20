import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { RegisterUser } from './features/users/pages/register-user/register-user';
import { Login } from './features/users/pages/login-user/login-user';
import { Header } from './layout/header/header';
import { Main } from './features/main/pages/main/main';

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
        path: 'register',
        component: RegisterUser
      },
      {
        path: 'login',
        component: Login
      },
      {
        path:'header',
        component: Header
      },

    ]
  
  }
  
];