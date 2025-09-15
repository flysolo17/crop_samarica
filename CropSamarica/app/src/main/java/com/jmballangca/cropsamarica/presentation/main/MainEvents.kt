package com.jmballangca.cropsamarica.presentation.main

import com.jmballangca.cropsamarica.data.models.rice_field.RiceField


sealed interface MainEvents {

    data class SelectRiceField(val riceField: RiceField) : MainEvents

}