package com.jmballangca.cropsamarica.domain.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.cropsamarica.domain.models.Developers
import com.jmballangca.cropsamarica.domain.models.Notifications
import com.jmballangca.cropsamarica.domain.models.UserGuide
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val firestore : FirebaseFirestore
): CommonRepository {
    private val developerRef = firestore.collection("developers")
    private val notificationsRef = firestore.collection("notifications")
    private val userGuideRef = firestore.collection("user-guide")
    override suspend fun getAllDevelopers(): List<Developers> {
        return developerRef.get().await().toObjects(Developers::class.java)
    }

    override suspend fun getAllUserGuides(): List<UserGuide> {
        return userGuideRef.get().await().toObjects(UserGuide::class.java)
    }

    override fun getAllMyNotifications(uid: String): Flow<List<Notifications>> {
        return callbackFlow {
            val listener = notificationsRef
                .whereEqualTo("uid", uid)
                .whereEqualTo("sent", true)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CommonRepositoryImpl", "Error getting notifications", error)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val notifications = snapshot.toObjects(Notifications::class.java)
                    trySend(notifications)
                } else {
                    trySend(emptyList())
                }
            }
            awaitClose {
                listener.remove()
            }
        }
    }

    override suspend fun getNotificationById(id: String): Result<Notifications?> {
        return try {
            val notification = notificationsRef.document(id).get().await().toObject(Notifications::class.java)
            if (notification?.status == "unseen") {
                updateNotificationStatus(id)
            }
            Result.success(notification)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNotificationStatus(id: String) {
        notificationsRef.document(id).update("status", "seen").await()
    }

    override suspend fun deleteNotification(id: String): Result<String> {
        return try {
            notificationsRef.document(id).delete().await()
            Result.success("Notification deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}