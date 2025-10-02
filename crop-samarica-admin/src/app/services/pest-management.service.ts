import { Injectable } from '@angular/core';
import {
  collection,
  collectionData,
  doc,
  docData,
  Firestore,
  updateDoc,
} from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { PestManagement } from '../models/PestManagement';

@Injectable({
  providedIn: 'root',
})
export class PestManagementService {
  private pestCollection = 'pest_and_diseases';
  constructor(private firestore: Firestore) {}

  getAll(): Observable<PestManagement[]> {
    const pestCollectionRef = collection(this.firestore, this.pestCollection);
    return collectionData(pestCollectionRef, { idField: 'id' }) as Observable<
      PestManagement[]
    >;
  }

  update(pest: PestManagement): Promise<void> {
    return updateDoc(doc(this.firestore, this.pestCollection, pest.id), {
      ...pest,
    });
  }
  getPestById(id: string): Observable<PestManagement> {
    const pestDocRef = doc(this.firestore, this.pestCollection, id);
    return docData(pestDocRef, { idField: 'id' }) as Observable<PestManagement>;
  }
}
