package com.jmballangca.cropsamarica.domain.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.cropsamarica.data.models.pest.PestAndDisease
import com.jmballangca.cropsamarica.domain.repository.PestAndDiseasesRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PestAndDiseasesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): PestAndDiseasesRepository {
    private val pestAndDiseasesCollection = firestore.collection("pest_and_diseases")

    override suspend fun insertAll(pestAndDiseases: List<PestAndDisease>) {
        val batch = firestore.batch()
        pestAndDiseases.forEach { pestAndDisease ->
            val docRef = pestAndDiseasesCollection.document()
            val new = pestAndDisease.copy(id = docRef.id)
            batch.set(docRef, new)
        }
        batch.commit().await()
    }

    override suspend fun getAll(): List<PestAndDisease> {
        return pestAndDiseasesCollection.get().await().toObjects(PestAndDisease::class.java)
    }

    override fun getById(id: String): Flow<PestAndDisease?> {
        return callbackFlow {
            val listener =
                pestAndDiseasesCollection.document(id).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(null)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val pestAndDisease = snapshot.toObject(PestAndDisease::class.java)
                        trySend(pestAndDisease)
                    } else {
                        trySend(null)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }
}