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


