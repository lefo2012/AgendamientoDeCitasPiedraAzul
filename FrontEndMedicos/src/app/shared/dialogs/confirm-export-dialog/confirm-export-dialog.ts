import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';

export interface ConfirmExportDialogData {
  appointmentCount: number;
}

@Component({
  selector: 'app-confirm-export-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule],
  templateUrl: './confirm-export-dialog.html',
  styleUrl: './confirm-export-dialog.scss',
})
export class ConfirmExportDialog {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmExportDialogData,
    private readonly dialogRef: MatDialogRef<ConfirmExportDialog>
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
