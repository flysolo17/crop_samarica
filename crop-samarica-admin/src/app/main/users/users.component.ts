import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { TabHeaderComponent } from '../../common/tab-header/tab-header.component';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {
  debounceTime,
  distinctUntilChanged,
  Subject,
  Subscription,
} from 'rxjs';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Users } from '../../models/Users';
import { MatCardModule } from '@angular/material/card';
@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule,
    TabHeaderComponent,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
})
export class UsersComponent implements OnInit, OnDestroy {
  private userSubscription: Subscription | null = null;
  loading: boolean = false;
  private allData: Users[] = [];
  data: Users[] = [];
  private searchSub: Subscription | null = null;
  searchTerm: string = '';

  private searchTerm$ = new Subject<string>();
  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.getAll();
    this.searchSub = this.searchTerm$
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe((term) => {
        this.applySearch(term);
      });
  }
  getAll(): void {
    this.loading = true;
    this.userSubscription = this.userService.getAll().subscribe({
      next: (users) => {
        this.allData = users;
        this.data = users;

        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        this.loading = false;
      },
    });
  }
  ngOnDestroy(): void {
    this.userSubscription?.unsubscribe();
    this.searchSub?.unsubscribe();
  }

  onSearchTermChange(term: string): void {
    this.searchTerm = term;
    this.loading = true;
    this.searchTerm$.next(term);
  }

  // Applies filtering after debounce
  private applySearch(term: string): void {
    this.data = this.allData.filter((item) =>
      item.name.toLowerCase().includes(term.toLowerCase())
    );
    this.loading = false;
  }
}
