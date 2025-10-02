import { Component, OnDestroy, OnInit } from '@angular/core';
import { CarouselComponent } from '../../../common/carousel/carousel.component';

import { CommonModule, Location } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressBar } from '@angular/material/progress-bar';
import { ActivatedRoute } from '@angular/router';
import { PestManagementService } from '../../../services/pest-management.service';
import { Subscription } from 'rxjs';
import { PestManagement } from '../../../models/PestManagement';

import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

import { MatExpansionModule } from '@angular/material/expansion';
import { MatBadgeModule } from '@angular/material/badge';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-view-pest-and-disease',
  standalone: true,
  imports: [
    CarouselComponent,
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatProgressBar,
    MatToolbarModule,
    MatInputModule,
    MatFormFieldModule,
    MatExpansionModule,
    MatBadgeModule,
    MatChipsModule,
  ],
  providers: [],

  templateUrl: './view-pest-and-disease.component.html',
  styleUrl: './view-pest-and-disease.component.scss',
})
export class ViewPestAndDiseaseComponent implements OnInit, OnDestroy {
  loading = false;
  id: string | null = null;
  subsription: Subscription | null = null;
  pestManagement$: PestManagement | null = null;
  constructor(
    private activatedRoute: ActivatedRoute,
    private pestManagement: PestManagementService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      this.id = params.get('id');
      if (this.id) {
        this.fetchData(this.id);
      }
    });
  }
  fetchData(id: string) {
    this.loading = true;
    this.subsription = this.pestManagement.getPestById(id).subscribe((data) => {
      this.pestManagement$ = data;
    });
    setTimeout(() => {
      this.loading = false;
    }, 2000);
  }
  ngOnDestroy(): void {
    this.subsription?.unsubscribe();
  }
  goBack() {
    this.location.back();
  }
}
