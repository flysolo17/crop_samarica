package com.jmballangca.cropsamarica.presentation.navigation
import kotlinx.serialization.Serializable

@Serializable
object ONBOARDING

@Serializable
object CREATE_CROP_FIELD
@Serializable
object AUTH


@Serializable
object MAIN
@Serializable
data class HOME(
    val id : String ?= null)
@Serializable
object CHAT

@Serializable
object TASK

@Serializable
object SETTINGS
@Serializable
object PROFILE

@Serializable
data class SURVEY(
    val id : String
)

@Serializable
data object PEST_AND_DISEASES

@Serializable
data class PEST_AND_DISEASES_DETAIL(
    val id : String
)


@Serializable
data object DEVELOPERS

@Serializable
data object USER_GUIDE


@Serializable
data class WEATHER_FORECAST(
    val id : String
)


@Serializable
data class VIEW_CROP_FIELD(
    val id : String
)


@Serializable
data object NOTIFICATIONS


@Serializable
data class VIEW_NOTIFICATION(
    val id : String
)