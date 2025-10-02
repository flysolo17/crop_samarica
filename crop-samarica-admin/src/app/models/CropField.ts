import { QueryDocumentSnapshot } from '@angular/fire/firestore';

export interface CropField {
  id: string;
  uid: string;
  name: string;
  location: string;
  areaSize: number;
  irrigationType: string;
  soilType: string;
  variety: string;
  stage: string;
  status: string;
  plantedDate: number;
  expectedHarvestDate: number;
  createdAt: Date;
  updatedAt: Date;
}

export const CropFieldConverter = {
  toFirestore: (data: CropField) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const data = snap.data() as CropField;
    data.createdAt = (data.createdAt as any).toDate();
    data.updatedAt = (data.updatedAt as any).toDate();
    return data;
  },
};
