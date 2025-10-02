import { Inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class ToasterService {
  constructor(private snackBar: MatSnackBar) {}

  success(message: string, action: string = 'OK', duration: number = 3000) {
    this.snackBar.open(message, action, {
      duration,
      panelClass: ['toaster-success'],
    });
  }

  error(message: string, action: string = 'Dismiss', duration: number = 3000) {
    this.snackBar.open(message, action, {
      duration,
      panelClass: ['toaster-error'],
    });
  }

  info(message: string, action: string = 'Got it', duration: number = 3000) {
    this.snackBar.open(message, action, {
      duration,
      panelClass: ['toaster-info'],
    });
  }
}
