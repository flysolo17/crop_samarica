import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  Form,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ToasterService } from '../../services/toaster.service';
import { MatDialog } from '@angular/material/dialog';
import { ForgotPasswordComponent } from '../forgot-password/forgot-password.component';
import { A11yModule } from '@angular/cdk/a11y';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    A11yModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  loginForm$: FormGroup;
  loading = false;
  errorMessage: string | null = null;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toaster: ToasterService,
    private dialog: MatDialog
  ) {
    this.loginForm$ = this.fb.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }
  submit() {
    if (this.loginForm$.invalid) {
      this.errorMessage = 'Please fill in all required fields.';
      this.toaster.error(this.errorMessage);
      return;
    }

    this.loading = true;
    const { email, password } = this.loginForm$.value;

    this.authService
      .login(email!, password!) // `!` to assure TypeScript since form is valid
      .then((admin) => {
        if (!admin) {
          this.errorMessage = 'Invalid login credentials.';
          this.toaster.error(this.errorMessage);
          return;
        }

        this.authService.setUser(admin);
        this.router.navigate(['/main']);
        this.toaster.success('Login successful!');
      })
      .catch((error) => {
        this.toaster.error(error.message || 'Login failed. Please try again.');
        console.error('Login error:', error);
      })
      .finally(() => {
        this.loading = false;
      });
  }

  openForgotPasswordDialog() {
    this.dialog.open(ForgotPasswordComponent);
  }
}
