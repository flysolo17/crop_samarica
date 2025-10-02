import { Component } from '@angular/core';
import { TabHeaderComponent } from '../../common/tab-header/tab-header.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreateDeveloperComponent } from './create-developer/create-developer.component';
import { MatCardModule } from '@angular/material/card';
import { MatCommonModule } from '@angular/material/core';
import { MatChipsModule } from '@angular/material/chips';
import { CommonModule } from '@angular/common';
import { DeveloperService } from '../../services/developer.service';
import { DeveloperRoles } from '../../models/Developers';

@Component({
  selector: 'app-developers',
  standalone: true,
  imports: [
    TabHeaderComponent,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatCardModule,
    CommonModule,
    MatChipsModule,
    MatCommonModule,
  ],
  templateUrl: './developers.component.html',
  styleUrl: './developers.component.scss',
})
export class DevelopersComponent {
  developers$ = this.developerService.getAll();
  constructor(
    private dialog: MatDialog,
    private developerService: DeveloperService
  ) {}
  openDialog() {
    this.dialog.open(CreateDeveloperComponent);
  }
  formatRoles(roles: DeveloperRoles[]): string {
    if (!roles || roles.length === 0) return '';
    return roles.map((r) => r.replace(/_/g, ' ')).join(' / ');
  }
}
