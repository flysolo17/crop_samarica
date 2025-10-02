import { firestore, messaging } from "./firebase";
import { FieldValue } from "firebase-admin/firestore";
import { onDocumentCreated } from "firebase-functions/firestore";
import * as logger from "firebase-functions/logger";
import { onSchedule } from "firebase-functions/scheduler";

export type NotificationType =
  | "rice_field"
  | "task"
  | "reminders"
  | "announcements";

export type NotificationStatus = "seen" | "unseen";

export interface NotificationPayload {
  id?: string;
  uid: string;
  action: string;
  type: NotificationType;
  title: string;
  message: string;
  schedule: Date | null;
  status?: NotificationStatus;
  riceFieldId?: string;
  sent: boolean;
}

export function randomStringGenerator(length = 10): string {
  const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  let result = "";
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}

export async function createNotification({
  uid,
  type,
  title,
  message,
  sent = true,
  status = "unseen",
  riceFieldId,
  action,
  id = randomStringGenerator(),
  schedule = null,
}: NotificationPayload) {
  try {
    const payload = {
      id,
      uid,
      type,
      title,
      message,
      schedule,
      status,
      riceFieldId: riceFieldId ?? null,
      action,
      sent: sent,
      timestamp: FieldValue.serverTimestamp(),
    };

    await firestore.collection("notifications").doc(id).set(payload);

    logger.info(`Notification created: ${type} for riceField ${riceFieldId}`, {
      uid,
    });
  } catch (err) {
    logger.error("Error creating notification:", err);
  }
}

export const onNotificationCreated = onDocumentCreated(
  "notifications/{docId}",
  async (event) => {
    const data = event.data?.data();

    if (!data) return;

    const { uid, title, message, type, riceFieldId, status, schedule, sent } =
      data;

    try {
      if (schedule == null && sent === true) {
        await messaging.send({
          topic: uid,
          notification: { title, body: message },
          data: {
            type,
            riceFieldId: riceFieldId ?? "",
            status,
          },
        });
      }
      logger.info(`Notification sent to uid: ${uid}`);
    } catch (err) {
      logger.error("Error sending notification:", err);
    }
  }
);

export const processUnsentNotifications = onSchedule(
  {
    schedule: "47 5 * * *",
    timeZone: "Asia/Manila",
  },
  async () => {
    logger.info("Running scheduled notification processor (5:05 AM PH)...");

    try {
      const now = new Date();
      const startOfDay = new Date(
        now.getFullYear(),
        now.getMonth(),
        now.getDate(),
        0,
        0,
        0
      );
      const endOfDay = new Date(
        now.getFullYear(),
        now.getMonth(),
        now.getDate(),
        23,
        59,
        59
      );

      const snapshot = await firestore
        .collection("notifications")
        .where("schedule", ">=", startOfDay)
        .where("schedule", "<=", endOfDay)
        .where("sent", "==", false)
        .get();

      logger.info(`Found ${snapshot.size} unsent notifications.`);

      const batch = firestore.batch();
      const promises: Promise<any>[] = [];

      snapshot.forEach((doc) => {
        const { uid, title, message, type, riceFieldId, status } = doc.data();

        promises.push(
          messaging.send({
            topic: uid,
            notification: { title, body: message },
            data: {
              type,
              riceFieldId: riceFieldId ?? "",
              status,
            },
          })
        );

        batch.update(doc.ref, {
          sent: true,
          sentAt: FieldValue.serverTimestamp(),
        });
      });

      await Promise.all(promises);
      await batch.commit();

      logger.info("Finished processing unsent notifications (5:05 AM run).");
    } catch (err) {
      logger.error("Error processing unsent notifications:", err);
    }
  }
);
