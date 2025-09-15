package com.jmballangca.cropsamarica.presentation.profile

import com.jmballangca.cropsamarica.data.models.user.User


data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null
)