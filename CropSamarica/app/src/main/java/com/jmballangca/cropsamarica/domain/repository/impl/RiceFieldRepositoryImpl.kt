package com.jmballangca.cropsamarica.domain.repository.impl

import android.R.attr.text
import android.content.Context
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.cropsamarica.data.models.rice_field.Announcement
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceFieldWithWeather
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.asAnnouncement
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.weather.Weather
import com.jmballangca.cropsamarica.data.service.WeatherApiService
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.domain.repository.ForecastRepository
import com.jmballangca.cropsamarica.domain.repository.RiceFieldRepository
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import com.jmballangca.cropsamarica.domain.repository.impl.AyaRepositoryImpl.Companion.CREATE_CROP_FIELD
import com.jmballangca.cropsamarica.domain.utils.toDateOnly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonNull.content
import kotlinx.serialization.json.jsonObject
import java.util.Date
import javax.inject.Inject
import kotlin.collections.map
import androidx.core.content.edit

class RiceFieldRepositoryImpl @Inject constructor(
    private val context: Context,
    private val ai: GenerativeModel,
    private val firestore: FirebaseFirestore,
    private val forecastRepository: ForecastRepository,
    private val taskRepository: TaskRepository,
    private val weatherService: WeatherApiService
): RiceFieldRepository {
    private val sharedPref = context.getSharedPreferences(
        "my_app_prefs",
        Context.MODE_PRIVATE
    )
    fun setSelectedField(fieldId: String) {
        sharedPref.edit { putString("selected_field_id", fieldId) }
    }
    fun getSelectedFieldId(): String? {
        return sharedPref.getString("selected_field_id", null)
    }
    private val announcementRef = firestore.collection("announcements")
    override fun getAllByUid(uid: String): Flow<List<RiceField>> {
        return callbackFlow {
            val listener = firestore.collection("rice_fields")
                .whereEqualTo("uid", uid)
                .orderBy("createdAt")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error) // emit error
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val fields = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(RiceField::class.java)
                        }
                        trySend(fields).isSuccess
                    }
                }
            awaitClose { listener.remove() }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRiceFieldWithWeather(uid: String): Flow<List<RiceFieldWithWeather>> {
        val riceFieldsFlow = callbackFlow {
            val listener = firestore.collection("rice_fields")
                .whereEqualTo("uid", uid)
                .orderBy("createdAt")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val fields = snapshot?.toObjects(RiceField::class.java) ?: emptyList()
                        trySend(fields)
                    }
                }
            awaitClose { listener.remove() }
        }

        return riceFieldsFlow.flatMapLatest { fields ->
            if (fields.isEmpty()) return@flatMapLatest flowOf(emptyList())
            val fieldIds = fields.map { it.id }

            // fetch weather once per field set
            val weatherMap = fields.associateWith { field ->
                forecastRepository.getWeather(field.location).getOrNull()
            }

            taskRepository.getAllByFieldIds(fieldIds).map { tasks ->
                fields.map { field ->
                    val fieldTasks = tasks.filter { it.fieldId == field.id }
                    RiceFieldWithWeather(
                        riceField = field,
                        weather = weatherMap[field],
                        tasks = fieldTasks
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRiceField(riceFieldId: String): Flow<RiceFieldWithWeather> {
        val riceFieldFlow = callbackFlow {
            val listener = firestore.collection("rice_fields")
                .document(riceFieldId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {

                        return@addSnapshotListener
                    }
                    val field = snapshot?.toObject(RiceField::class.java)
                    if (field != null) {
                        trySend(field)
                    }
                }
            awaitClose { listener.remove() }
        }

        return riceFieldFlow.flatMapLatest { field ->
            flow {
                val weather = forecastRepository.getDailyForecast(
                    location = field.location,
                    days = 1
                ).getOrNull()
                emitAll(
                    taskRepository.getAllByFieldIds(listOf(field.id)).map { tasks ->
                        val fieldTasks = tasks.filter { it.fieldId == field.id }
                        RiceFieldWithWeather(
                            riceField = field,
                            weather = weather,
                            tasks = fieldTasks,
                            announcements = generateAnnouncement(
                                riceField = field,
                                weather = weather,
                                tasks = fieldTasks
                            )
                        )
                    }
                )
            }
        }
    }

    override fun getRiceFieldWithId(riceFieldId: String): Flow<RiceField> {
        return callbackFlow{
            val listener = firestore.collection("rice_fields")
                .document(riceFieldId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    val field = snapshot?.toObject(RiceField::class.java)
                    if (field != null) {
                        trySend(field)
                    }
                }
            awaitClose { listener.remove() }
        }

    }

    override suspend fun deleteCropField(id: String): Result<String> {
        return try {
            val batch = firestore.batch()

            // Delete tasks
            val tasks = firestore.collection("tasks")
                .whereEqualTo("fieldId", id)
                .get()
                .await()
            tasks.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            // Delete announcements
            val announcements = firestore.collection("announcements")
                .whereEqualTo("fieldId", id)
                .get()
                .await()
            announcements.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            // Delete reminders (note: uses "riceFieldId")
            val reminders = firestore.collection("reminders")
                .whereEqualTo("riceFieldId", id)
                .get()
                .await()
            reminders.documents.forEach { doc ->
                batch.delete(doc.reference)
            }


            val fieldRef = firestore.collection("rice_fields").document(id)
            batch.delete(fieldRef)


            batch.commit().await()

            Result.success("Crop field deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }





    suspend fun generateAnnouncement(
        riceField: RiceField,
        weather: DailyForecast?,
        tasks: List<Task>
    ): Announcement? {
        val now = Date()

        val existing = announcementRef
            .whereEqualTo("fieldId", riceField.id)
            .whereEqualTo("date", now.toDateOnly())
            .get()
            .await()

        if (!existing.isEmpty) {
            return existing.documents.first().toObject(Announcement::class.java)
        }

        val prompt = content {
            text(
                """
            Based on the following information, generate exactly one clear and useful announcement for the farmer. 
            Keep it short, simple, and actionable. 
            Use farmer-friendly language and avoid technical terms.

            Rice Field:
            $riceField

            Weather today:
            $weather

            Upcoming Tasks:
            $tasks

            Output format:
            - Title: very short (max 5 words).
            - Message: 1–2 simple sentences with the most important advice or warning for today.
            - Urgency: LOW, MEDIUM, or HIGH depending on how important or time-sensitive the message is.
            """
            )
        }
        val response = ai.generateContent(prompt)
        val functionCall = response.functionCalls.find { it.name == ANOUNCEMENT }

        val announcementJson = functionCall?.args?.get("announcement")?.jsonObject ?: return null

        val announcement = announcementJson.asAnnouncement(
            fieldId = riceField.id,
        ).copy(id = announcementRef.document().id)

        announcementRef.document(announcement.id)
            .set(announcement)
            .await()
        return announcement
    }


    companion object {
        const val ANOUNCEMENT = "announcement"
        val CREATE_ANNOUNCEMENT = FunctionDeclaration(
            name = ANOUNCEMENT,
            description = "Generates one clear and practical announcement for rice farmers based on the rice field data, weather forecast, and tasks.",
            parameters = mapOf(
                "announcement" to Schema.obj(
                    mapOf(
                        "title" to Schema.string("A short title for the announcement."),
                        "message" to Schema.string("A short, clear, practical, and concise, farmer-friendly message."),
                        "urgency" to Schema.enumeration(
                            listOf("LOW", "MEDIUM", "HIGH"),
                            "Level of urgency for this announcement."
                        )
                    )
                )
            )
        )
    }

}