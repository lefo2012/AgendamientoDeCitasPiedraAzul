import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';

export interface ConfirmCancelDialogData {
  appointment: {
    doctorName: string;
    patientName: string;
    date: string;
    appointmentInterval: string;
  };
}

@Component({
  selector: 'app-confirm-cancel-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule],
  templateUrl: './confirm-cancel-dialog.html',
  styleUrl: './confirm-cancel-dialog.scss',
})
export class ConfirmCancelDialog {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmCancelDialogData,
    private readonly dialogRef: MatDialogRef<ConfirmCancelDialog>
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
