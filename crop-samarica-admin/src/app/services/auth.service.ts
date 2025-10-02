import { Injectable, signal } from '@angular/core';
import {
  Auth,
  createUserWithEmailAndPassword,
  onAuthStateChanged,
  sendPasswordResetEmail,
  signInWithEmailAndPassword,
  signOut,
  User,
} from '@angular/fire/auth';
import {
  collection,
  doc,
  docData,
  Firestore,
  getDoc,
  setDoc,
  updateDoc,
} from '@angular/fire/firestore';
import { Administrator, AdministratorConverter } from '../models/Administrator';
import { BehaviorSubject, catchError, map, Observable, of } from 'rxjs';
import {
  getDownloadURL,
  ref,
  Storage,
  uploadBytes,
} from '@angular/fire/storage';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _reference = collection(this.firestore, 'administrator');
  private currentUserSubject = new BehaviorSubject<Administrator | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  setUser(user: Administrator | null) {
    this.currentUserSubject.next(user);
  }

  constructor(
    private firestore: Firestore,
    private auth: Auth,
    private storage: Storage
  ) {}

  async changeProfile(file: File) {
    try {
      const user = this.auth.currentUser as User;
      if (!user) throw new Error('No user logged in');

      // Path in Firebase Storage
      const filePath = `profiles/${user.uid}/${file.name}`;
      const fileRef = ref(this.storage, filePath);

      // Upload file
      await uploadBytes(fileRef, file);

      // Get download URL
      const downloadURL = await getDownloadURL(fileRef);

      // Update Firestore user document
      const userDocRef = doc(this.firestore, `administrator/${user.uid}`);
      await updateDoc(userDocRef, { profile: downloadURL });

      return downloadURL;
    } catch (e) {
      console.error('Error updating profile picture:', e);
      throw e;
    }
  }

  get currentUser() {
    return this.currentUserSubject.value;
  }
  setCurrentUser(user: Administrator | null) {
    this.currentUserSubject.next(user);
  }
  async login(email: string, password: string): Promise<Administrator | null> {
    try {
      const result = await signInWithEmailAndPassword(
        this.auth,
        email,
        password
      );
      const id = result.user.uid;

      const docSnap = await getDoc(doc(this._reference, id));

      if (!docSnap.exists()) {
        return null;
      }

      return docSnap.data() as Administrator;
    } catch (error) {
      console.error('Login failed:', error);
      return null;
    }
  }

  async register(
    email: string,
    password: string,
    name: string,
    profile: string
  ): Promise<Administrator | null> {
    try {
      const result = await createUserWithEmailAndPassword(
        this.auth,
        email,
        password
      );
      const id = result.user.uid;
      const data: Administrator = {
        id: id,
        email: email,
        name: name,
        profile: profile,
      };
      await setDoc(
        doc(this._reference, id).withConverter(AdministratorConverter),
        data
      );
      return data;
    } catch (error) {
      console.error('Registration failed:', error);
      return null;
    }
  }
  async getUserById(id: string): Promise<Administrator | null> {
    const docSnap = await getDoc(
      doc(this._reference, id).withConverter(AdministratorConverter)
    );
    if (!docSnap.exists()) {
      return null;
    }
    return docSnap.data() as Administrator;
  }
  observeUser(): Observable<Administrator | null> {
    const uid = this.auth.currentUser?.uid;
    if (!uid) {
      return of(null);
    }

    return docData(
      doc(this._reference, uid).withConverter(AdministratorConverter)
    ).pipe(
      map((user) => user ?? null),
      catchError((err) => {
        console.error('Error observing user:', err);
        return of(null);
      })
    );
  }

  forgetPassword(email: string) {
    return sendPasswordResetEmail(this.auth, email);
  }
  editProfile(id: string, name: string) {
    return updateDoc(doc(this._reference, id), {
      name: name,
    });
  }

  logout() {
    return signOut(this.auth);
  }
}
