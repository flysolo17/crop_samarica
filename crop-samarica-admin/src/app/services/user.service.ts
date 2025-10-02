import { Injectable } from '@angular/core';
import {
  collection,
  collectionData,
  Firestore,
  or,
  orderBy,
  query,
} from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { UserConverter, Users } from '../models/Users';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private usersCollection = 'users';
  constructor(private firestere: Firestore) {}

  getAll(): Observable<Users[]> {
    const q = query(
      collection(this.firestere, this.usersCollection).withConverter(
        UserConverter
      ),
      orderBy('name', 'asc')
    );
    return collectionData(q, { idField: 'id' }) as Observable<Users[]>;
  }
}
