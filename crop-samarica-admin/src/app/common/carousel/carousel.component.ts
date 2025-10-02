import { Component, Input } from '@angular/core';

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { NgIf, NgFor } from '@angular/common';
@Component({
  selector: 'app-carousel',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, NgIf, NgFor],
  templateUrl: './carousel.component.html',
  styleUrl: './carousel.component.scss',
})
export class CarouselComponent {
  @Input({
    required: true,
  })
  images: string[] = [];
  currentIndex: number = 0;

  next() {
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
  }
  prev() {
    this.currentIndex =
      (this.currentIndex - 1 + this.images.length) % this.images.length;
  }
}
