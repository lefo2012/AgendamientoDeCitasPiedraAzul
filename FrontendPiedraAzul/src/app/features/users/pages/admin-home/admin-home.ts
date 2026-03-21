import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.html'
})
export class AdminHome {

  constructor(private router: Router) {}

  irARegistrarMedico() {
    this.router.navigate(['/admin/registrar-medico']);
  }
}