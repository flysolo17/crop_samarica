import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';

import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterModule } from '@angular/router';
import { Administrator } from '../models/Administrator';
import { AuthService } from '../services/auth.service';
import { Subscription } from 'rxjs';

export interface MainNavigation {
  link: string;
  label: string;
  icon: string;
}

export const mainNavigation: MainNavigation[] = [
  { link: 'dashboard', label: 'Dashboard', icon: 'dashboard' },
  { link: 'users', label: 'Users', icon: 'person' },
  { link: 'crops', label: 'Crops', icon: 'grass' },
  { link: 'pest-management', label: 'Pest Management', icon: 'bug_report' },
];

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    RouterModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    CommonModule,
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent implements OnInit, OnDestroy {
  navItems$ = mainNavigation;
  user$: Administrator | null = null;
  userSub: Subscription | null = null;
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.userSub = this.authService.observeUser().subscribe((data) => {
      this.user$ = data;
    });
    console.log(this.user$);
  }
  ngOnDestroy(): void {
    this.userSub?.unsubscribe();
  }
}
