import { QueryDocumentSnapshot } from '@angular/fire/firestore';

export interface Administrator {
  id: string;
  email: string;
  name: string;
  profile: string;
}

export const AdministratorConverter = {
  toFirestore: (data: Administrator) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const data = snap.data() as Administrator;
    return data;
  },
};
