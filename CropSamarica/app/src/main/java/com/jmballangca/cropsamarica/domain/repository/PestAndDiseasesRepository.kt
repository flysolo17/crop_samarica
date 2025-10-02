package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.data.models.pest.PestAndDisease
import kotlinx.coroutines.flow.Flow


interface PestAndDiseasesRepository {
    suspend fun insertAll(
        pestAndDiseases: List<PestAndDisease>
    )
    suspend fun getAll() : List<PestAndDisease>
    fun getById(
        id : String
    ) : Flow<PestAndDisease?>


}