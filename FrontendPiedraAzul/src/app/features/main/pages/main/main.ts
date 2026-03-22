import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreateAppointment } from '../../../appointments/pages/create-appointment/create-appointment';



@Component({
  selector: 'app-main',
  imports: [MatDialogModule],
  templateUrl: './main.html',
  styleUrl: './main.scss',
})
export class Main  {

  constructor(private router: Router, private dialog: MatDialog) {}

 
  goToAbout() {
    this.router.navigate(['/about']);
  }

  goToCreateAppointment() {
    this.dialog.open(CreateAppointment, {
      width: '50%',
      height: '50%',
      disableClose: false
    });
  }

  goToRescheduleAppointment() {
    this.router.navigate(['/citas/reagendar']);
  }

  goToCancelAppointment() {
    this.router.navigate(['/citas/cancelar']);
  }

}
