import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';

export interface NoPermissionDialogData {
  message?: string;
}

@Component({
  selector: 'app-no-permission-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogModule, MatDividerModule, MatIconModule],
  templateUrl: './no-permission-dialog.html',
  styleUrl: './no-permission-dialog.scss',
})
export class NoPermissionDialog {
  readonly message: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) data: NoPermissionDialogData | null,
    private readonly dialogRef: MatDialogRef<NoPermissionDialog>
  ) {
    this.message = data?.message ?? 'Tu cuenta no tiene permisos para gestionar citas.';
  }

  close(): void {
    this.dialogRef.close();
  }
}
