import { Injectable } from '@angular/core';
import {
  Firestore,
  collection,
  collectionData,
  or,
  orderBy,
  query,
  where,
} from '@angular/fire/firestore';
import { CropField, CropFieldConverter } from '../models/CropField';
import { catchError, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CropFieldService {
  private cropFieldsCollection = 'rice_fields';
  constructor(private firestere: Firestore) {}

  getAll(): Observable<CropField[]> {
    const q = query(
      collection(this.firestere, this.cropFieldsCollection).withConverter(
        CropFieldConverter
      ),
      orderBy('plantedDate', 'asc')
    );

    return collectionData(q, { idField: 'id' }).pipe(
      catchError((err) => {
        console.error('Error fetching crop fields:', err);
        // Return an empty array as a fallback so subscribers still get a value
        return of([] as CropField[]);
      })
    );
  }

  getByUser(uid: string): Observable<CropField[]> {
    const q = query(
      collection(this.firestere, this.cropFieldsCollection).withConverter(
        CropFieldConverter
      ),
      where('uid', '==', uid),
      orderBy('plantedDate', 'asc')
    );
    return collectionData(q, { idField: 'id' }) as Observable<CropField[]>;
  }
}
