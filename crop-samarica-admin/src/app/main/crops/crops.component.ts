import {
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  debounceTime,
  distinctUntilChanged,
  Subject,
  Subscription,
} from 'rxjs';
import { CropField } from '../../models/CropField';
import { CropFieldService } from '../../services/crop-field.service';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { TabHeaderComponent } from '../../common/tab-header/tab-header.component';
import { CommonModule } from '@angular/common';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { RiceStage, RiceStages } from '../../models/PestManagement';
export type RiceStagesWithIcons = {
  stage: RiceStage;
  name: string;
  icon: string;
};
@Component({
  selector: 'app-crops',
  standalone: true,
  imports: [
    MatTableModule,
    MatProgressBarModule,
    TabHeaderComponent,
    CommonModule,
    MatTableModule,

    MatPaginatorModule,
  ],
  templateUrl: './crops.component.html',
  styleUrl: './crops.component.scss',
})
export class CropsComponent implements OnInit, OnDestroy, AfterViewInit {
  private subscription: Subscription | null = null;
  private searchSub: Subscription | null = null;
  loading = false;
  searchTerm: string = '';

  dataSource = new MatTableDataSource<CropField>([]);

  private ALL: CropField[] = [];
  private searchTerm$ = new Subject<string>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  constructor(private cropFieldService: CropFieldService) {}

  displayedColumns: string[] = [
    'name',
    'location',
    'stage',
    'planted',
    'harvest',
  ];

  riceStages: RiceStagesWithIcons[] = [
    {
      stage: RiceStages.SEEDLING,
      name: 'Seedling',
      icon: '../../assets/images/seedling.webp',
    },
    {
      stage: RiceStages.TILLERING,
      name: 'Tillering',
      icon: '../../assets/images/tillering.webp',
    },
    {
      stage: RiceStages.STEM_ELONGATION,
      name: 'Stem Elongation',
      icon: '../../assets/images/stem_elongation.webp',
    },
    {
      stage: RiceStages.PANICLE_INITIATION,
      name: 'Panicle Initiation',
      icon: '../../assets/images/panicle_initiation.webp',
    },
    {
      stage: RiceStages.BOOTING,
      name: 'Booting',
      icon: '../../assets/images/booting.webp',
    },
    {
      stage: RiceStages.FLOWERING,
      name: 'Flowering',
      icon: '../../assets/images/flowering.webp',
    },
    {
      stage: RiceStages.MILKING,
      name: 'Milking',
      icon: '../../assets/images/milking.webp',
    },
    {
      stage: RiceStages.DOUGH,
      name: 'Dough',
      icon: '../../assets/images/dough.webp',
    },
    {
      stage: RiceStages.MATURE,
      name: 'Mature',
      icon: '../../assets/images/mature.webp',
    },
  ];

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.searchSub?.unsubscribe();
  }
  ngOnInit(): void {
    this.initializeData();
  }
  initializeData() {
    this.loading = true;
    this.subscription = this.cropFieldService.getAll().subscribe((data) => {
      this.ALL = data;
      this.dataSource.data = this.ALL;
      this.loading = false;
    });

    this.searchSub = this.searchTerm$
      .pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe((term) => {
        this.applySearch(term);
      });
  }

  private applySearch(term: string): void {
    this.dataSource.data = this.ALL.filter((item) =>
      item.name.toLowerCase().includes(term.toLowerCase())
    );
    this.loading = false;
  }
  onSearchTermChange(term: string): void {
    this.searchTerm = term;
    this.loading = true;
    this.searchTerm$.next(term);
  }
  toDate(dateInMillis: number): string {
    if (!dateInMillis) return '';
    const date = new Date(dateInMillis);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: '2-digit',
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  count(stage: string): number {
    let count = 0;
    this.ALL.forEach((e) => {
      if (e.stage === stage) {
        count += 1;
      }
    });
    return count;
  }
}
