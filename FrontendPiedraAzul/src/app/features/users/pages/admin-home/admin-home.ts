import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.html',
  styleUrl: './admin-home.scss',
  imports: [
    MatButtonModule,
    MatCardModule,
    MatIconModule
  ]
})
export class AdminHome {
  private readonly router = inject(Router);

  irARegistrarMedico(): void {
    this.router.navigate(['/admin/registrar-medico']);
  }
}