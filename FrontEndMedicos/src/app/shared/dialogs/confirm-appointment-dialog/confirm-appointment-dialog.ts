import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';

export interface ConfirmAppointmentDialogData {
  doctorName: string;
  patientName: string;
  date: string;
  time: string;
}

@Component({
  selector: 'app-confirm-appointment-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule],
  templateUrl: './confirm-appointment-dialog.html',
  styleUrl: './confirm-appointment-dialog.scss',
})
export class ConfirmAppointmentDialog {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmAppointmentDialogData,
    private readonly dialogRef: MatDialogRef<ConfirmAppointmentDialog>
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
