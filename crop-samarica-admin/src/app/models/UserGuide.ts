import { QueryDocumentSnapshot } from '@angular/fire/firestore';

export interface UserGuide {
  id: string;
  title: string;
  description?: string | null;
  video: string | null;
  createdAt: Date;
  updatedAt: Date;
}

export const UserGuideConverter = {
  toFirestore: (data: UserGuide) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const data = snap.data() as UserGuide;
    data.createdAt = (data.createdAt as any).toDate();
    data.updatedAt = (data.updatedAt as any).toDate();
    return data;
  },
};
