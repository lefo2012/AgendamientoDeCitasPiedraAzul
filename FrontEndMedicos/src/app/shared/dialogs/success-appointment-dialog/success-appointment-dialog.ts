import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';

export interface SuccessAppointmentDialogData {
  doctorName: string;
  patientName: string;
  date: string;
  time: string;
}

@Component({
  selector: 'app-success-appointment-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogModule, MatIconModule, MatDividerModule],
  templateUrl: './success-appointment-dialog.html',
  styleUrl: './success-appointment-dialog.scss',
})
export class SuccessAppointmentDialog {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: SuccessAppointmentDialogData,
    private readonly dialogRef: MatDialogRef<SuccessAppointmentDialog>
  ) {}

  onClose(): void {
    this.dialogRef.close(true);
  }
}
