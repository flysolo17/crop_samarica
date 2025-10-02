import { Component } from '@angular/core';
import { UserGuideService } from '../../../services/user-guide.service';
import { ToasterService } from '../../../services/toaster.service';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { UserGuide } from '../../../models/UserGuide';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-create-user-guide',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDividerModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    MatCardModule,
    CommonModule,
  ],
  templateUrl: './create-user-guide.component.html',
  styleUrl: './create-user-guide.component.scss',
})
export class CreateUserGuideComponent {
  userGuideForm: FormGroup;
  loading = false;

  constructor(
    private userGuideService: UserGuideService,
    private toastr: ToasterService,
    private fb: FormBuilder,
    private matDialogRef: MatDialogRef<CreateUserGuideComponent>
  ) {
    this.userGuideForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(500)]],
      video: ['', [Validators.required, youtubeUrlValidator()]],
    });
  }

  save() {
    if (this.userGuideForm.invalid) return;
    this.loading = true;

    const now = new Date();
    const userGuide: UserGuide = {
      id: '',
      ...this.userGuideForm.value,
      createdAt: now,
      updatedAt: now,
    };

    this.userGuideService
      .create(userGuide)
      .then(() => {
        this.toastr.success('User Guide created successfully');
        this.matDialogRef.close(true);
      })
      .catch(() => {
        this.toastr.error('Failed to create user guide');
      })
      .finally(() => (this.loading = false));
  }

  close() {
    this.matDialogRef.close();
  }
}

export function youtubeUrlValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) return null; // allow required validator to handle empty case

    const youtubeRegex = /^(https?:\/\/)?(www\.)?(youtube\.com|youtu\.be)\/.+$/;

    return youtubeRegex.test(control.value)
      ? null
      : { invalidYoutubeUrl: true };
  };
}
