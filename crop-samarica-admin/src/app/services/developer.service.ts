import { Injectable } from '@angular/core';
import {
  collection,
  collectionData,
  doc,
  Firestore,
  setDoc,
} from '@angular/fire/firestore';
import { Developers } from '../models/Developers';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DeveloperService {
  private readonly developerCollection = 'developers';
  constructor(private firestore: Firestore) {}

  create(developer: Developers) {
    let uid = doc(collection(this.firestore, this.developerCollection)).id;
    let newDev = { ...developer, id: uid };
    return setDoc(doc(this.firestore, this.developerCollection, uid), newDev);
  }

  getAll(): Observable<Developers[]> {
    return collectionData(
      collection(this.firestore, this.developerCollection)
    ) as Observable<Developers[]>;
  }
}
