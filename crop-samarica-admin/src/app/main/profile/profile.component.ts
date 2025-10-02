import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatDialog } from '@angular/material/dialog';
import { Administrator } from '../../models/Administrator';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { ToasterService } from '../../services/toaster.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, CommonModule, MatListModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent {
  admin$ = this.authService.observeUser();
  constructor(
    private authService: AuthService,
    private dialog: MatDialog,
    private toastr: ToasterService,
    private router: Router
  ) {}

  editProfile(admin: Administrator) {
    const open = this.dialog.open(EditProfileComponent, {
      data: admin,
      width: '600px',
    });
    open.afterClosed().subscribe((data) => {
      if (data != null) {
        this.authService.editProfile(admin.id, data).then(() => {
          this.toastr.success('Successfully Updated!');
        });
      }
    });
  }
  logout() {
    this.authService.logout().then(() => {
      this.toastr.success('Successfully Logged out!');
      this.router.navigate(['auth']);
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.upload(file);
    }
  }
  upload(file: File) {
    this.authService
      .changeProfile(file)
      .then(() => {
        this.toastr.success('Successfully Updated');
      })
      .catch((e) => {
        this.toastr.error(e['message'] ?? 'Unknown error');
      });
  }
}
