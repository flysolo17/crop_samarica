import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserGuideService } from '../../services/user-guide.service';
import { UserGuide } from '../../models/UserGuide';
import {
  debounceTime,
  distinctUntilChanged,
  Subject,
  Subscription,
} from 'rxjs';
import { TabHeaderComponent } from '../../common/tab-header/tab-header.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { CreateUserGuideComponent } from './create-user-guide/create-user-guide.component';
import { RouterLink } from '@angular/router';
import { DeleteConfirmationDialogComponent } from '../../common/delete-confirmation-dialog/delete-confirmation-dialog.component';
import { ToasterService } from '../../services/toaster.service';
import { Title } from 'chart.js';
import { SafeUrlPipe } from './view-user-guide/SafeUrlPipe';
import { UserGuideCardComponent } from './user-guide-card/user-guide-card.component';

@Component({
  selector: 'app-user-guide',
  standalone: true,
  imports: [
    TabHeaderComponent,
    MatProgressBarModule,
    MatButtonModule,
    MatIconModule,
    CommonModule,
    MatCardModule,
    RouterLink,
    SafeUrlPipe,
    UserGuideCardComponent,
  ],
  templateUrl: './user-guide.component.html',
  styleUrl: './user-guide.component.scss',
})
export class UserGuideComponent implements OnInit, OnDestroy {
  private ALL: UserGuide[] = [];
  userGuides$: UserGuide[] = [];
  private userGuideSub: Subscription | null = null;
  loading: boolean = false;

  private searchSub: Subscription | null = null;
  searchTerm: string = '';
  private searchTerm$ = new Subject<string>();
  constructor(
    private userGuideService: UserGuideService,
    private dialog: MatDialog,
    private toastr: ToasterService
  ) {}

  ngOnInit(): void {
    this.initialize();
  }
  ngOnDestroy(): void {
    this.searchSub?.unsubscribe();
    this.userGuideSub?.unsubscribe();
  }
  initialize() {
    this.loading = true;
    this.userGuideSub = this.userGuideService.getAll().subscribe((data) => {
      this.ALL = data;
      this.userGuides$ = this.ALL;
      this.loading = false;
    });
    this.searchSub = this.searchTerm$
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe((term) => {
        this.applySearch(term);
      });
  }

  onSearchTermChange(term: string): void {
    this.searchTerm = term;
    this.loading = true;
    this.searchTerm$.next(term);
  }

  private applySearch(term: string): void {
    this.userGuides$ = this.ALL.filter((item) =>
      item.title.toLowerCase().includes(term.toLowerCase())
    );
    this.loading = false;
  }
  createNew() {
    this.dialog.open(CreateUserGuideComponent, {
      width: '650px',
    });
  }
}
