import { QueryDocumentSnapshot } from '@angular/fire/firestore';

export interface Users {
  id: string;
  name: string;
  email: string;
  profile: string;
}

export const UserConverter = {
  toFirestore: (data: Users) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const data = snap.data() as Users;
    return data;
  },
};
