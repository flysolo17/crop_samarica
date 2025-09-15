package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.user.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(name: String, email: String, password: String): Result<String>
    suspend fun logout(): Result<String>
    suspend fun getUser(): Result<User?>

    fun getUserRealTime(): Flow<User?>

    suspend fun forgotPassword(
        email: String
    ) : Result<String>


    suspend fun signInWithGoogle() : Result<String>
    suspend fun registerWithGoogle() : Result<String>

    suspend fun changeName(
        uid : String,
        name : String,
        result : (UIState<String>) -> Unit
    )
    suspend fun changePassword(
        currentPassword : String,
        newPassword : String,
        result : (UIState<String>) -> Unit
    )
}