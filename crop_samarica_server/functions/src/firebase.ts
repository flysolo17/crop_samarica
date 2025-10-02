import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";
import { getMessaging } from "firebase-admin/messaging";

const app = initializeApp();
export const firestore = getFirestore(app);
export const messaging = getMessaging(app);
