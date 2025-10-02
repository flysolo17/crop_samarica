import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  debounceTime,
  distinctUntilChanged,
  Subject,
  Subscription,
} from 'rxjs';
import { PestManagementService } from '../../services/pest-management.service';
import { PestManagement } from '../../models/PestManagement';
import { TabHeaderComponent } from '../../common/tab-header/tab-header.component';
import { CommonModule } from '@angular/common';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { UpdatePestDialogComponent } from './update-pest-dialog/update-pest-dialog.component';
import { ToasterService } from '../../services/toaster.service';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-pest-and-disease',
  standalone: true,
  imports: [
    TabHeaderComponent,
    CommonModule,
    MatProgressBarModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    RouterLink,
  ],
  templateUrl: './pest-and-disease.component.html',
  styleUrl: './pest-and-disease.component.scss',
})
export class PestAndDiseaseComponent implements OnInit, OnDestroy {
  private subscription: Subscription | null = null;

  loading: boolean = false;
  private allData: PestManagement[] = [];
  data: PestManagement[] = [];

  errorMessage: string | null = null;
  private searchSub: Subscription | null = null;
  searchTerm: string = '';

  private searchTerm$ = new Subject<string>();

  constructor(
    private pestManagementService: PestManagementService,
    private matDialog: MatDialog,
    private toastr: ToasterService
  ) {}

  ngOnInit(): void {
    this.initializeData();

    // Subscribe to debounced search
    this.searchSub = this.searchTerm$
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe((term) => {
        this.applySearch(term);
      });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.searchSub?.unsubscribe();
  }
  openDialog(data: PestManagement): void {
    const dialogRef = this.matDialog.open(UpdatePestDialogComponent, {
      data,
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.update(result);
    });
  }

  update(data: PestManagement): void {
    this.pestManagementService
      .update(data)
      .then(() => {
        this.toastr.success('Pest data updated successfully.');
      })
      .catch((error) => {
        this.toastr.error('Failed to update pest data.');
        console.error('Error updating pest data:', error);
      });
  }

  initializeData(): void {
    this.loading = true;
    this.subscription = this.pestManagementService.getAll().subscribe({
      next: (data) => {
        this.allData = data; // keep original
        this.data = data; // initialize filtered
        this.loading = false;
        console.log(data);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = 'Failed to load pest and disease data.';
        console.error('Error fetching pest and disease data:', err);
      },
    });
  }

  // Called from template (input event)
  onSearchTermChange(term: string): void {
    this.searchTerm = term;
    this.loading = true;
    this.searchTerm$.next(term);
  }

  // Applies filtering after debounce
  private applySearch(term: string): void {
    this.data = this.allData.filter((item) =>
      item.title.toLowerCase().includes(term.toLowerCase())
    );
    this.loading = false;
  }

  trackById(index: number, item: PestManagement): string {
    return item.id;
  }
}
