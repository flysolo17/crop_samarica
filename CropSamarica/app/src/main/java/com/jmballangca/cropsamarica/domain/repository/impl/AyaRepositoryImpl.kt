package com.jmballangca.cropsamarica.domain.repository.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import coil.util.CoilUtils.result
import com.google.android.gms.tasks.Task
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.cropsamarica.core.utils.CREATE_REMINDER
import com.jmballangca.cropsamarica.core.utils.REMINDER
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.core.utils.asReminder
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStatus
import com.jmballangca.cropsamarica.data.models.rice_field.asRecommendation
import com.jmballangca.cropsamarica.data.models.rice_field.asRiceField
import com.jmballangca.cropsamarica.data.models.rice_field.getRiceStage
import com.jmballangca.cropsamarica.data.models.weather.SevenDayWeatherResponse
import com.jmballangca.cropsamarica.data.service.WeatherApiService
import com.jmballangca.cropsamarica.data.models.weather.Weather
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.domain.models.Recommendation
import com.jmballangca.cropsamarica.domain.models.RecommendationResult
import com.jmballangca.cropsamarica.domain.models.questions.Question
import com.jmballangca.cropsamarica.domain.models.questions.QuestionType
import com.jmballangca.cropsamarica.domain.models.questions.QuestionsWithCurrentStage
import com.jmballangca.cropsamarica.domain.models.questions.asQuestion
import com.jmballangca.cropsamarica.domain.models.toDailyForecastUI


import com.jmballangca.cropsamarica.domain.repository.AyaRepository
import com.jmballangca.cropsamarica.domain.repository.SevenDayWeatherForecast

import com.jmballangca.cropsamarica.domain.utils.toBitmap
import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldForm
import com.jmballangca.cropsamarica.presentation.survey.QuestionWithAnswers

import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.util.Date

import javax.inject.Inject


class AyaRepositoryImpl @Inject constructor(
    private val context : Context,
    private val ai: GenerativeModel,
    private val auth: FirebaseAuth,
    private val weatherApiService: WeatherApiService,
    private val firestore : FirebaseFirestore,
): AyaRepository {
    private val riceFieldRef = firestore.collection("rice_fields")
    private val weatherRef = firestore.collection("weather")
    private val reminderRef = firestore.collection("reminders")
    private fun saveData(
        uid: String,
        args: Map<String, JsonElement>,
        weather: DailyForecast
    ) : Task<String> {
        val batch = firestore.batch()
        val riceField = args.asRiceField()
        val riceFieldId = riceFieldRef.document().id
        riceField.id = riceFieldId
        riceField.uid = uid
        riceField.location = weather.location
        batch.set(riceFieldRef.document(riceFieldId), riceField)
        val weatherId = weatherRef.document().id
        weather.id = weatherId
        batch.set(weatherRef.document(weatherId), weather)
        return batch.commit().continueWith { task ->
            if (task.isSuccessful) {
                Log.d("AyaRepositoryImpl", "saveData: ${task.result}")
                riceField.id
            } else {
                throw task.exception ?: Exception("Unknown Firestore error")
            }
        }
    }
    private suspend fun getWeather(location: String) : Result<DailyForecast> {
        return try {
            val response = weatherApiService.getWeather(location)
            if (response.isSuccessful) {
                val weather = response.body()
                if (weather != null) {
                    val converted = weather.toDailyForecastUI()
                    Result.success(converted)
                } else {
                    Result.failure(Exception("Weather data is null"))
                }
            } else {
                Result.failure(Exception("Failed to fetch weather data"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateCropField(data: CreateCropFieldForm): Result<List<Recommendation>> {
        return try {
            val uid = auth.currentUser?.uid
            if (uid == null) {
                return Result.failure(Exception("User not logged in"))
            }

            val weatherResult = getWeather(data.location)
            val prompt = """
                You are an expert rice agronomist. 
                Generate a JSON object strictly following the riceFieldSchema based on the provided farmer input and 7-day weather forecast.
                
                Farmer Input:
                - Name: ${data.name}
                - Location: ${data.location}
                - Soil Type: ${data.soilType}
                - Area (hectares): ${data.area}
                - Planted Date (raw input): ${data.plantedDate}
                - Variety: ${data.variety}
                - Last Fertilizer Applied: ${data.lastFertilizer}
                - Irrigation: ${data.irrigation}
            
                Weather Forecast:
                $${weatherResult}
            
                Requirements:
                - Output must be **valid JSON only**, no extra text.
                - Follow the riceFieldSchema exactly.
                - Dates:
                  * `planted_date`: Must be in **epoch millis UTC** (convert input if not epoch).
                  * `expected_harvest_date`: Must be in **epoch millis UTC**, calculated from planted_date + variety duration.
                - Generate realistic values for:
                  * stage (from RiceStage) 
                  * status (from RiceStatus)
                  * recommendations (at least 2, each tied to stage, short and based on weather).
            """.trimIndent()


            val response = ai.generateContent(prompt)

            val functionCalls = response.functionCalls.find { it.name == CREATE_CROP_FIELD }
            if (functionCalls != null) {
                val data = saveData(uid,functionCalls.args, weatherResult.getOrThrow()).await()
                val recommendations = functionCalls.args["recommendations"]?.jsonArray?.mapNotNull { recElement ->
                    recElement.jsonObject.asRecommendation().copy(
                        fieldId = data
                    )
                } ?: emptyList()
                Log.d("AyaRepositoryImpl", "generateCropField: $recommendations")
                Result.success(recommendations)
            } else {
                Result.failure(Exception("No function calls found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateRecommendation(
        data: RiceField,
        imageUri: Uri?,
    ): Result<RecommendationResult> {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            return Result.failure(Exception("User not logged in"))
        }

        return try {
            // STEP 1: Save rice field to Firestore
            val batch = firestore.batch()
            val riceFieldId = riceFieldRef.document().id
            data.id = riceFieldId
            data.uid = uid

            val riceFieldDoc = riceFieldRef.document(riceFieldId)
            batch.set(riceFieldDoc, data)
            batch.commit().await()

            val location = data.location
            val forecast = weatherApiService.getWeather(location, days = 7)
            val prompt = content {
                text(
                    """
                    You are an expert agricultural advisor specializing in rice farming in the Philippines. 
                    Your task is to generate clear, stage-specific recommendations for the farmer. 
                    
                    Consider the following:
                    - Rice field data (location, stage, size, etc.): $data
                    - 7-day weather forecast: $forecast
                    
                    Guidelines:
                    1. Recommendations must be practical, actionable, and aligned with local farming practices.  
                    2. Take into account weather risks (rain, drought, pests) and crop growth stage.  
                    3. Prioritize sustainable and cost-effective methods where possible.  
                    4. Output recommendations strictly in this format:
                    
                    [
                      {
                        "stage": "<RiceStage>",
                        "title": "<Short title summarizing recommendation>",
                        "details": "<Detailed but concise explanation of what the farmer should do and why. maximum of 2 sentences>"
                      }
                    ]
                    """
                )

                // Optional image input if available
                imageUri.toBitmap(context)?.let { bitmap ->
                    image(bitmap)
                }
            }


            val response = ai.generateContent(prompt)
            val functionCalls = response.functionCalls.find { it.name == CREATE_CROP_FIELD }
            if (functionCalls != null) {

                val recommendations = functionCalls.args["recommendations"]
                    ?.jsonArray
                    ?.mapNotNull { recElement ->
                        recElement.jsonObject.asRecommendation()
                    } ?: emptyList()
                Log.d("AyaRepositoryImpl", "generateCropField: $recommendations")
                Result.success(RecommendationResult(recommendations, riceFieldId))
            } else {
                Result.failure(Exception("No function calls found"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateSurveyForLastStage(id : String ): Result<QuestionsWithCurrentStage> {
        return try {
            val data = riceFieldRef.document(id).get().await().toObject(RiceField::class.java)
            if (data == null) {
                return Result.failure(Exception("Rice field not found"))
            }
            val currentStage = data.stage
            val nextStage = Date(data.plantedDate).getRiceStage()

            val prompt = content {
                text(
                    """
                You are an agricultural expert specializing in rice farming in the Philippines. 
                Generate 2 to 5 short, clear survey questions for the farmer, based on their rice field stage.  

                Consider:
                - Current stage: $currentStage
                - Next stage: $nextStage
                - Rice field data: $data

                Guidelines:
                1. Each question must include the text and the expected answer type.
                2. Answer types: MULTIPLE_CHOICE,SINGLE_CHOICE, TRUE_FALSE, SHORT_ANSWER , LONG_ANSWER.
                3. If MULTIPLE_CHOICE and SINGLE_CHOICE, provide 3–4 practical options.
                4. Questions must be simple and farmer-friendly.
                """
                )
            }

            val response = ai.generateContent(prompt)

            val functionCalls = response.functionCalls.find { it.name == QUESTION_GENERATION }
            Log.d("AyaRepositoryImpl", "generateSurvey: ${functionCalls?.args}")
            if (functionCalls != null) {
                val questions = functionCalls.args["questions"]
                    ?.jsonArray
                    ?.mapNotNull { element ->
                        element.jsonObject.asQuestion()
                    } ?: emptyList()

                Log.d("AyaRepositoryImpl", "generateSurvey: $questions")
                val result = QuestionsWithCurrentStage(
                    questions = questions,
                    riceField = data
                )
                Result.success(result)
            } else {
                Result.failure(Exception("No QUESTION_GENERATION function call found"))
            }

        } catch (e: Exception) {
            Log.e("AyaRepositoryImpl", "generateSurvey: $e",e)
            Result.failure(e)
        }
    }

    override suspend fun generateRecommendationForNextStageBasedOnTheSurvey(
        riceField: RiceField,
        survey : List<QuestionWithAnswers>,
        imageUri : Uri?
    ): Result<RecommendationResult> {
        return try {
            val currentStage = riceField.stage
            val nextState = Date(riceField.plantedDate).getRiceStage()
            if (currentStage == nextState) {
                return Result.failure(Exception("You already in the current stage"))
            }
            riceFieldRef.document(
                riceField.id
            ).update(
                "stage", nextState,
                "updatedAt" , Date()
            ).await()
            val prompt = content {
                imageUri?.toBitmap(context)?.let {
                    image(it)
                }
                text("""
                     You are an agricultural expert specializing in rice farming in the Philippines. 
                Based on the farmer’s rice field data and survey answers, provide 2–5 clear and actionable recommendations 
                for the next stage of rice growth. 

                Consider:
                - Current stage: $currentStage
                - Next stage: $nextState
                - Rice field data: $riceField
                - Survey responses: $survey

                   Guidelines:
                    1. Recommendations must be practical, actionable, and aligned with local farming practices.  
                    2. Take into account weather risks (rain, drought, pests) and crop growth stage.  
                    3. Prioritize sustainable and cost-effective methods where possible.  
                    4. Output recommendations strictly in this format:
                    
                    [
                      {
                        "stage": "${nextState}",
                        "title": "<Short title summarizing recommendation>",
                        "details": "<Detailed but concise explanation of what the farmer should do and why. maximum of 2 sentences>"
                      }
                    ]
                """.trimIndent())
            }
            val response = ai.generateContent(prompt)
            val functionCalls = response.functionCalls.find { it.name == CREATE_CROP_FIELD }
            if (functionCalls != null) {

                val recommendations = functionCalls.args["recommendations"]
                    ?.jsonArray
                    ?.mapNotNull { recElement ->
                        recElement.jsonObject.asRecommendation()
                    } ?: emptyList()
                Log.d("AyaRepositoryImpl", "generateCropField: $recommendations")

                Result.success(RecommendationResult(recommendations = recommendations, riceField.id))
            } else {
                Result.failure(Exception("No function calls found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateWeatherForecast(id: String): Result<SevenDayWeatherForecast> {
        return  try {
            val riceField = riceFieldRef.document(id).get().await().toObject(RiceField::class.java) ?: return Result.failure(Exception("Rice field not found"))
            val sevenDayWeatherResponse = weatherApiService.getSevenDayWeatherForecast(
                location = riceField.location,
                days = 10
            )
            if (!sevenDayWeatherResponse.isSuccessful) {
                return Result.failure(Exception("Failed to fetch weather data"))
            }
            val weather = sevenDayWeatherResponse.body() ?: return Result.failure(Exception("Weather data is null"))
            Log.d(
                "weather",
                "weather: $weather"
            )
            Log.d("weather", "forecast days: ${weather.forecast.forecastday.size}")

            Result.success(SevenDayWeatherForecast(riceField = riceField, sevenDayWeatherResponse = weather))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateReminder(
        riceField: RiceField,
        weather : SevenDayWeatherResponse
    ) : Result<List<Reminder>> {
        return try {
            val prompt = content {
                text("""
                    Generate atleast  3 actionable reminders based on ricefield data and weather forecast for the next 
                    3 days. here's the data:
                     
                    RiceField Data: ${riceField},
                    Weather Forecast: ${weather}
                """.trimIndent()
                )
            }
            Log.d("AyaRepositoryImpl", "generateReminder: $prompt")
            val response = ai.generateContent(prompt)
            val functionCalls = response.functionCalls.find { it.name == REMINDER }
            Log.d("AyaRepositoryImpl", "generateReminder: ${functionCalls?.args}")
            if (functionCalls != null) {
                val recommendations = functionCalls.args["reminders"]
                    ?.jsonArray
                    ?.mapNotNull { recElement ->
                        recElement.jsonObject.asReminder()
                    } ?: emptyList()
                Result.success(
                    recommendations
                )
            } else {
                Result.failure(Exception("No function calls found"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    companion object {
        const val QUESTION_GENERATION = "QUESTION_GENERATION"

        val QUESTION_GENERATION_DECLARATION = FunctionDeclaration(
            name = QUESTION_GENERATION,
            description = "Generates tailored rice crop survey questions for farmers based on the current crop stage. " +
                    "Each question should include the question text, answer type, and optional multiple-choice options.",
            parameters = mapOf(
                "questions" to Schema.array(
                    Schema.obj(
                        mapOf(
                            "text" to Schema.string(
                                description = "The actual survey question to ask the farmer. Keep it short and simple."
                            ),
                            "type" to Schema.enumeration(
                                QuestionType.entries.map { it.name },
                                description = "The type of answer expected for this question. One of: MULTIPLE_CHOICE,SINGLE_CHOICE, TRUE_FALSE, SHORT_ANSWER , LONG_ANSWER."
                            ),

                            "options" to Schema.array(
                                Schema.string(
                                    description = "An option for the question if the answerType is 'multiple_choice'."
                                ),
                                description = "List of available options for multiple-choice questions.",
                                nullable = true
                            )
                        )
                    ),
                    description = "List of 2 to 5 survey questions for the farmer to answer.",
                    nullable = false
                )
            )
        )
        const val CREATE_CROP_FIELD = "CREATE_CROP_FIELD"

        val CREATE_RICE_FIELD_DECLARATION = FunctionDeclaration(
            name = CREATE_CROP_FIELD,
            description = "Generates tailored rice crop recommendations based on farmer input and weather conditions.",
            parameters = mapOf(
                "recommendations" to Schema.array(
                    Schema.obj(
                        mapOf(
                            "stage" to Schema.enumeration(
                                RiceStage.entries.map { it.name },
                                description = "The growth stage of the rice crop for which this recommendation applies."
                            ),
                            "title" to Schema.string("A short title summarizing the recommendation."),
                            "details" to Schema.string(
                                "A short, clear explanation of the recommendation. Keep it simple, easy to read, and practical for farmers.Maximum of 2 sentences"
                            )
                        )
                    ),
                    description = "List of AI-generated, stage-specific recommendations for rice crops.",
                    nullable = false
                )
            )
        )

    }
}