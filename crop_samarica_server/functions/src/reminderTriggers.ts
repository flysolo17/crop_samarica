import { onDocumentCreated } from "firebase-functions/firestore";
import * as logger from "firebase-functions/logger";
import { createNotification, randomStringGenerator } from "./notification";

export const onReminderCreated = onDocumentCreated(
  "reminders/{docId}",
  async (event) => {
    const data = event.data?.data();

    if (!data) {
      logger.warn("Reminder created but no data found", {
        docId: event.params.docId,
      });
      return;
    }

    const id = data.id ?? event.params.docId ?? randomStringGenerator();
    const uid = data.uid ?? "unknown";
    const riceFieldId = data.riceFieldId ?? null;
    const message = data.message ?? "New reminder created";

    const schedule =
      data.reminderDate && data.reminderDate.toDate
        ? data.reminderDate.toDate()
        : null;

    let sent = false;
    if (schedule) {
      const today = new Date();
      const isToday =
        schedule.getFullYear() === today.getFullYear() &&
        schedule.getMonth() === today.getMonth() &&
        schedule.getDate() === today.getDate();

      sent = isToday;
    }

    logger.info("Reminder created", { id, uid, riceFieldId, schedule, sent });

    await createNotification({
      id: randomStringGenerator(),
      uid,
      type: "reminders",
      title: "New Reminder",
      riceFieldId,
      message,
      schedule,
      sent,
      action: "REMINDER_CREATED",
    });
  }
);
