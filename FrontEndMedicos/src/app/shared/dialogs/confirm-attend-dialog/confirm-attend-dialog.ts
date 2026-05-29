import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmCancelDialogData } from '../confirm-cancel-dialog/confirm-cancel-dialog';

export interface ConfirmAttendDialogData {
  appointment: {
    doctorName: string;
    patientName: string;
    date: string;
    appointmentInterval: string;
  };
}

@Component({
  selector: 'app-confirm-attend-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule],
  templateUrl: './confirm-attend-dialog.html',
  styleUrl: './confirm-attend-dialog.scss',
})
export class ConfirmAttendDialog {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmAttendDialogData,
    private readonly dialogRef: MatDialogRef<ConfirmAttendDialog>
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
