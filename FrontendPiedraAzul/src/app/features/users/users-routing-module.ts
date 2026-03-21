import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './pages/login-user/login-user';
import { RegisterUser } from './pages/register-user/register-user';
import { AdminHome } from './pages/admin-home/admin-home';

const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: RegisterUser },
  {
    path: 'admin',
    component: AdminHome
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
