package com.jmballangca.cropsamarica.presentation.auth

import com.jmballangca.cropsamarica.data.models.user.User


data class AuthState(
    val isLoading: Boolean = false,
    val userEntity: User? = null,
    val isRegistering: Boolean = false,
    val isSubmitting: Boolean = false,

)