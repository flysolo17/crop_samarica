package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceFieldWithWeather
import kotlinx.coroutines.flow.Flow

interface RiceFieldRepository {
    fun getAllByUid(uid: String): Flow<List<RiceField>>

    fun getRiceFieldWithWeather(
        uid: String,
    ) : Flow<List<RiceFieldWithWeather>>

    fun getRiceField(
        riceFieldId: String
    ) : Flow<RiceFieldWithWeather>

    fun getRiceFieldWithId(
        riceFieldId: String,
    ) : Flow<RiceField>


   suspend fun deleteCropField(
        id: String
    ) : Result<String>

}