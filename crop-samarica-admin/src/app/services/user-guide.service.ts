import { Injectable } from '@angular/core';
import { UserGuide, UserGuideConverter } from '../models/UserGuide';
import {
  collection,
  collectionData,
  deleteDoc,
  doc,
  docData,
  Firestore,
  orderBy,
  query,
  setDoc,
} from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root',
})
export class UserGuideService {
  private readonly userGuideCollection = 'user-guide';
  constructor(private firestore: Firestore, private sanitizer: DomSanitizer) {}

  create(userGuide: UserGuide) {
    const id = doc(collection(this.firestore, this.userGuideCollection)).id;
    userGuide.id = id;
    const userGuideDocRef = doc(
      collection(this.firestore, this.userGuideCollection).withConverter(
        UserGuideConverter
      ),
      id
    );
    return setDoc(userGuideDocRef, userGuide);
  }
  delete(id: string) {
    return deleteDoc(doc(this.firestore, this.userGuideCollection, id));
  }
  getAll(): Observable<UserGuide[]> {
    const q = query(
      collection(this.firestore, this.userGuideCollection).withConverter(
        UserGuideConverter
      ),
      orderBy('createdAt', 'asc')
    );
    return collectionData(q);
  }

  private cleanYoutubeUrl(url: string): string {
    if (!url) return '';

    try {
      const ytRegex =
        /^(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([a-zA-Z0-9_-]{11})/;
      const match = url.match(ytRegex);
      if (match && match[1]) {
        return `https://www.youtube.com/embed/${match[1]}`;
      }
    } catch (e) {
      console.error('Invalid YouTube URL:', url);
    }

    return url; // fallback (if regex fails)
  }

  prepareUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      this.cleanYoutubeUrl(url)
    );
  }
  getById(id: string): Observable<UserGuide | null> {
    const userGuideDocRef = doc(
      collection(this.firestore, this.userGuideCollection).withConverter(
        UserGuideConverter
      ),
      id
    );

    return docData(userGuideDocRef, {
      idField: 'id',
    }) as Observable<UserGuide | null>;
  }
}
