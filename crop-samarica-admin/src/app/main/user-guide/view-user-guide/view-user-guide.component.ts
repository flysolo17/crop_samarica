import { CommonModule, Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { UserGuideService } from '../../../services/user-guide.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserGuide } from '../../../models/UserGuide';
import { DeleteConfirmationDialogComponent } from '../../../common/delete-confirmation-dialog/delete-confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { ToasterService } from '../../../services/toaster.service';
import { A11yModule } from '@angular/cdk/a11y';
import { MatCard, MatCardModule } from '@angular/material/card';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SafeUrlPipe } from './SafeUrlPipe';

@Component({
  selector: 'app-view-user-guide',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
    CommonModule,
    A11yModule,
    MatCardModule,
    SafeUrlPipe,
  ],
  templateUrl: './view-user-guide.component.html',
  styleUrl: './view-user-guide.component.scss',
})
export class ViewUserGuideComponent implements OnInit, OnDestroy {
  userGuideSub: Subscription | null = null;
  loading: boolean = false;
  userGuide$: UserGuide | null = null;

  constructor(
    private userGuideService: UserGuideService,
    private activatedRoute: ActivatedRoute,
    private location: Location,
    private dialog: MatDialog,
    private toastr: ToasterService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.initializeApp(id);
      }
    });
  }

  ngOnDestroy(): void {
    this.userGuideSub?.unsubscribe();
  }

  initializeApp(id: string) {
    this.loading = true;
    this.userGuideSub = this.userGuideService.getById(id).subscribe({
      next: (data) => {
        this.userGuide$ = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  goBack() {
    this.location.back();
  }
  delete(userGuide: UserGuide) {
    const modal = this.dialog.open(DeleteConfirmationDialogComponent, {
      width: '400px',
      data: {
        title: 'Delete User Guide',
        description: `Are you sure you want to delete the guide "${userGuide.title}"? This action cannot be undone.`,
      },
    });

    modal.afterClosed().subscribe((confirmed) => {
      if (confirmed) {
        this.userGuideService
          .delete(userGuide.id)
          .then(() => {
            this.toastr.success('Successfully Deleted');
            this.goBack();
          })
          .catch(() => {
            this.toastr.error('Failed to delete user guide');
          });
      }
    });
  }

  transform(value: string): SafeResourceUrl {
    // convert normal YouTube link to embed link
    const embedUrl = value.replace('watch?v=', 'embed/');
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }
}
