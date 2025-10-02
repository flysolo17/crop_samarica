import { Component } from '@angular/core';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatDialog,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';
import { ToasterService } from '../../services/toaster.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    MatInputModule,
    MatProgressSpinner,
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss',
})
export class ForgotPasswordComponent {
  email = new FormControl('', [Validators.required, Validators.email]);
  loading = false;
  constructor(
    private dialog: MatDialogRef<ForgotPasswordComponent>,
    private authService: AuthService,
    private toastr: ToasterService
  ) {}

  submit() {
    if (this.email.invalid || this.email === null) {
      this.toastr.error('Please enter a valid email address.');
      return;
    }
    this.authService
      .forgetPassword(this.email.value!)
      .then(() => {
        this.toastr.success('Password reset link sent! Check your email.');
        this.dialog.close(true);
      })
      .catch((err) => {
        this.toastr.error(err?.message || 'Failed to send reset link.');
        this.loading = false;
      });
  }
  close() {
    this.dialog.close(false);
  }
}
