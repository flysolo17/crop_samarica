import {
  onDocumentCreated,
  onDocumentDeleted,
  onDocumentUpdated,
} from "firebase-functions/firestore";
import * as logger from "firebase-functions/logger";
import { createNotification, randomStringGenerator } from "./notification";
import { firestore } from "./firebase";
import { onSchedule } from "firebase-functions/scheduler";

// 🔹 Utility to build notifications for rice fields
async function notifyRiceFieldEvent(
  uid: string,
  riceFieldId: string,
  name: string,
  action: "CREATED" | "UPDATED" | "DELETED"
) {
  const titles: Record<typeof action, string> = {
    CREATED: "New Rice Field Added",
    UPDATED: "Rice Field Updated",
    DELETED: "Rice Field Removed",
  };

  const messages: Record<typeof action, string> = {
    CREATED: `Your rice field "${name}" has been created successfully.`,
    UPDATED: `Your rice field "${name}" has been updated successfully.`,
    DELETED: `Your rice field "${name}" has been deleted.`,
  };

  await createNotification({
    id: randomStringGenerator(),
    uid,
    type: "rice_field",
    title: titles[action],
    message: messages[action],
    riceFieldId,
    sent: true,
    action: `${action}`, // e.g. RICE_FIELD_CREATED
    schedule: null,
  });
}

// 🔹 Create
export const onRiceFieldCreated = onDocumentCreated(
  "rice_fields/{docId}",
  async (event) => {
    const data = event.data?.data();
    if (!data) return;

    const id = data.id ?? event.params.docId ?? randomStringGenerator();
    const name = data.name ?? "Unnamed Field";
    const uid = data.uid ?? "unknown";

    logger.info("Rice field created", { id, name, uid });

    await notifyRiceFieldEvent(uid, id, name, "CREATED");
  }
);

export const onRiceFieldUpdated = onDocumentUpdated(
  "rice_fields/{docId}",
  async (event) => {
    const after = event.data?.after.data();
    if (!after) return;

    const id = after.id ?? event.params.docId ?? randomStringGenerator();
    const name = after.name ?? "Unnamed Field";
    const uid = after.uid ?? "unknown";

    logger.info("Rice field updated", { id, name, uid });

    await notifyRiceFieldEvent(uid, id, name, "UPDATED");
  }
);

// 🔹 Delete
export const onRiceFieldDeleted = onDocumentDeleted(
  "rice_fields/{docId}",
  async (event) => {
    const data = event.data?.data();
    if (!data) return;

    const id = data.id ?? event.params.docId ?? randomStringGenerator();
    const name = data.name ?? "Unnamed Field";
    const uid = data.uid ?? "unknown";

    logger.info("Rice field deleted", { id, name, uid });

    await notifyRiceFieldEvent(uid, id, name, "DELETED");
  }
);

export const processRiceFieldsReadyForNextStage = onSchedule(
  {
    schedule: "0 8 * * *", // every day at 8 AM
    timeZone: "Asia/Manila",
    
  },
  async () => {
    try {
      const riceFieldsSnapshot = await firestore
        .collection("rice_fields")
        .get();

      if (riceFieldsSnapshot.empty) {
        logger.info("No rice fields found.");
        return;
      }

      for (const doc of riceFieldsSnapshot.docs) {
        const data = doc.data();
        const plantedDate = data.plantedDate?.toDate
          ? data.plantedDate.toDate()
          : null;
        const currentStage = data.stage;

        if (!plantedDate || !currentStage) continue;

        const expectedStage = getRiceStage(plantedDate);

        const readyForNextStage =
          currentStage !== "MATURE" && expectedStage !== currentStage;

        if (readyForNextStage) {
          logger.info(
            `Rice field ${doc.id} (${data.name}) is ready for next stage: ${expectedStage}`
          );

          await createNotification({
            uid: data.uid,
            type: "rice_field",
            title: "Rice Field Ready for Next Stage",
            message: `Your rice field "${data.name}" is now ready to move from ${currentStage} to ${expectedStage}.`,
            riceFieldId: doc.id,
            action: "stage_ready",
            schedule: null,
            sent: true,
          });
        }
      }
    } catch (err) {
      logger.error("Error processing rice fields:", err);
      throw err;
    }
  }
);

/**
 * Utility to calculate rice stage from planted date.
 * Mirrors your Kotlin logic.
 */
function getRiceStage(plantedDate: Date): string {
  const currentDate = new Date();
  const diffInMillis = currentDate.getTime() - plantedDate.getTime();
  const daysSincePlanting = Math.floor(diffInMillis / (1000 * 60 * 60 * 24));

  if (daysSincePlanting <= 14) return "SEEDLING";
  if (daysSincePlanting <= 30) return "TILLERING";
  if (daysSincePlanting <= 45) return "STEM_ELONGATION";
  if (daysSincePlanting <= 55) return "PANICLE_INITIATION";
  if (daysSincePlanting <= 65) return "BOOTING";
  if (daysSincePlanting <= 75) return "FLOWERING";
  if (daysSincePlanting <= 90) return "MILKING";
  if (daysSincePlanting <= 110) return "DOUGH";
  return "MATURE";
}
