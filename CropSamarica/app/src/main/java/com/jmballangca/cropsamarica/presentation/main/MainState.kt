package com.jmballangca.cropsamarica.presentation.main

import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.user.User


data class MainState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val riceFields : List<RiceField> = emptyList(),
    val selectedRiceField : RiceField? = null
)