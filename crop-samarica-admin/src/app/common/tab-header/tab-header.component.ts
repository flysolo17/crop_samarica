import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { MatDividerModule, MatDivider } from '@angular/material/divider';
import { MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
@Component({
  selector: 'app-tab-header',
  standalone: true,
  imports: [
    MatDivider,
    CommonModule,
    MatDividerModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
  ],
  templateUrl: './tab-header.component.html',
  styleUrl: './tab-header.component.scss',
})
export class TabHeaderComponent {
  @Input({ required: true }) title!: string;
  @Input() description?: string | null;
  @Input({ required: false }) showSearch: boolean = true;
  @Input() searchText?: string | null;
  @Output() searchTextChange = new EventEmitter<string>();

  onSearchTextChange(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.searchTextChange.emit(inputElement.value);
  }
}
