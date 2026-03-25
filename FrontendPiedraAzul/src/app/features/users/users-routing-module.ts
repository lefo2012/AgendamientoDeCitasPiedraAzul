import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './pages/login-user/login-user';
import { AdminHome } from './pages/admin-home/admin-home';
import { RegisterDoctor } from './pages/register-doctor/register-doctor';

const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: 'admin',
    component: AdminHome
  },
  {
    path: 'admin/registrar-medico',
    component: RegisterDoctor
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
