import { Component, inject, model } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogModule,
} from '@angular/material/dialog';

export interface DeleteDialogData {
  title: string;
  description: string;
}

@Component({
  selector: 'app-delete-confirmation-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './delete-confirmation-dialog.component.html',
  styleUrl: './delete-confirmation-dialog.component.scss',
})
export class DeleteConfirmationDialogComponent {
  readonly dialogRef = inject(MatDialogRef<DeleteConfirmationDialogComponent>);
  readonly data = inject<DeleteDialogData>(MAT_DIALOG_DATA);
  readonly title = model(this.data.title);
  readonly description = model(this.data.description);

  onNoClick(): void {
    this.dialogRef.close();
  }
  confirmed() {
    this.dialogRef.close(true);
  }
}
