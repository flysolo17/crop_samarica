import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Auth, authState, signOut } from '@angular/fire/auth';
import { from, map, switchMap } from 'rxjs';
import { Firestore, doc, getDoc } from '@angular/fire/firestore';
import { ToasterService } from '../services/toaster.service';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);
  const authService = inject(AuthService);
  const toasterService = inject(ToasterService);
  return authState(auth).pipe(
    switchMap((user) => {
      if (!user) {
        return from([router.createUrlTree(['/auth/login'])]);
      }

      return from(authService.getUserById(user.uid)).pipe(
        map((docSnap) => {
          if (docSnap) {
            return true;
          } else {
            signOut(auth);
            toasterService.error(
              'Access denied. Please contact administrator.'
            );
            return router.createUrlTree(['/auth/login']);
          }
        })
      );
    })
  );
};
