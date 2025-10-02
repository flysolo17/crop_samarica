import { Component, Input, OnInit } from '@angular/core';
import { UserGuide } from '../../../models/UserGuide';

import {
  MatCard,
  MatCardContent,
  MatCardTitle,
  MatCardSubtitle,
} from '@angular/material/card';

import { UserGuideService } from '../../../services/user-guide.service';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-user-guide-card',
  standalone: true,
  imports: [
    MatCard,
    MatCardContent,
    MatCardTitle,
    MatCardSubtitle,
    CommonModule,
  ],
  templateUrl: './user-guide-card.component.html',
  styleUrl: './user-guide-card.component.scss',
})
export class UserGuideCardComponent implements OnInit {
  @Input({ required: true }) userGuide!: UserGuide;

  constructor(private userGuideService: UserGuideService) {}
  ngOnInit(): void {}
  sanitizeUrl(url: string): any {
    return this.userGuideService.prepareUrl(url);
  }
}
