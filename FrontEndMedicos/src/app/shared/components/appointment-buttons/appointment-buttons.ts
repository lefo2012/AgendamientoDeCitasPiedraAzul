import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-appointment-buttons',
  standalone: true,
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './appointment-buttons.html',
  styleUrl: './appointment-buttons.scss',
})
export class AppointmentButtons {
  @Input() exportDisabled = false;
  @Input() cancelDisabled = false;
  @Input() rescheduleDisabled = false;
  @Input() selectedAppointmentId: number | null = null;  // ← el padre le pasa el ID
  @Output() exportRequested = new EventEmitter<void>();
  @Output() cancelRequested = new EventEmitter<void>();

  constructor(private router: Router) {}

  goToNewAppointment(): void {
    this.router.navigate(['/citas/nueva']);
  }

  onExportClick(): void {
    this.exportRequested.emit();
  }

  onCancelClick(): void {
    this.cancelRequested.emit();
  }

  goToReschedule(): void {
  if (!this.selectedAppointmentId) return;
  this.router.navigate(['/citas/reagendar', this.selectedAppointmentId]);
}
}