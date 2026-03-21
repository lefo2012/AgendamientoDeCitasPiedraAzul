import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './pages/login-user/login-user';
import { RegisterUser } from './pages/register-user/register-user';

const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: RegisterUser }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
