import { Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { AppointmentList } from './features/appointments/pages/appointment-list/appointment-list';

export const routes: Routes = [

  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: '',
        component: AppointmentList
      }
    ]
  }

];