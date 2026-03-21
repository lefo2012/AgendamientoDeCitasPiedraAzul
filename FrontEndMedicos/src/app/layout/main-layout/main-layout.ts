import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../header/header';
import { AppointmentTable } from '../../features/appointments/pages/appointment-table/appointment-table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Footer } from '../footer/footer';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, Header, AppointmentTable, MatButtonModule, MatIconModule, Footer],
  templateUrl: './main-layout.html',
  styleUrls: ['./main-layout.scss'],
})
export class MainLayout {
  userName = 'Luis Fierro'; //Ejemplo de como se accede al usuario  
}
