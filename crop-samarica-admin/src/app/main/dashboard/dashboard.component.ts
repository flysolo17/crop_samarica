import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { ChartConfiguration, ChartOptions } from 'chart.js';

import { Subscription } from 'rxjs';
import { CropField } from '../../models/CropField';
import { Users } from '../../models/Users';
import { CropFieldService } from '../../services/crop-field.service';
import { UserService } from '../../services/user.service';

import { BaseChartDirective } from 'ng2-charts';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatGridListModule, BaseChartDirective],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
  loading = false;
  userSub: Subscription | null = null;
  cropFieldSub: Subscription | null = null;
  users$: Users[] = [];
  crops$: CropField[] = [];

  // Chart configs
  cropsByStageLabels: string[] = [];
  cropsByStageData: number[] = [];

  cropsBySoilLabels: string[] = [];
  cropsBySoilData: number[] = [];

  cropsGrowthChart: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [{ data: [], label: 'Area Size (ha)' }],
  };

  chartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
  };

  constructor(
    private cropFieldService: CropFieldService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userSub = this.userService.getAll().subscribe((data) => {
      this.users$ = data;
    });

    this.cropFieldSub = this.cropFieldService.getAll().subscribe((data) => {
      this.crops$ = data;
      this.prepareCharts();
    });
  }

  ngOnDestroy(): void {
    this.userSub?.unsubscribe();
    this.cropFieldSub?.unsubscribe();
  }

  private prepareCharts() {
    // Group crops by stage
    const stageMap = new Map<string, number>();
    const soilMap = new Map<string, number>();

    this.crops$.forEach((c) => {
      stageMap.set(c.stage, (stageMap.get(c.stage) || 0) + 1);
      soilMap.set(c.soilType, (soilMap.get(c.soilType) || 0) + 1);
    });

    this.cropsByStageLabels = Array.from(stageMap.keys());
    this.cropsByStageData = Array.from(stageMap.values());

    this.cropsBySoilLabels = Array.from(soilMap.keys());
    this.cropsBySoilData = Array.from(soilMap.values());

    // Growth bar chart (area size by variety)
    const varieties = this.crops$.map((c) => c.variety);
    const sizes = this.crops$.map((c) => c.areaSize);

    this.cropsGrowthChart = {
      labels: varieties,
      datasets: [{ data: sizes, label: 'Area Size (ha)' }],
    };
  }

  get harvested(): number {
    let count = 0;
    this.crops$.filter((e) => {
      if (e.stage === 'MATURE') {
        count += 1;
      }
    });
    return count;
  }

  get activeCrops(): number {
    let count = 0;
    this.crops$.filter((e) => {
      if (e.stage !== 'MATURE') {
        count += 1;
      }
    });
    return count;
  }
}
