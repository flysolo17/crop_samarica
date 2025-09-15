package com.jmballangca.cropsamarica.presentation.create_crop_field

import android.net.Uri
import com.jmballangca.cropsamarica.data.models.rice_field.IrrigationType
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.domain.models.RecommendationResult

import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypes
import java.util.Date


data class CreateCropFieldState(
    val isLoading : Boolean = false,
    val recommendationResult : RecommendationResult? = null,
    val isCreatingTask : Boolean = false,
    val name : String = "",
    val location : String = "",
    val plantedDate : Date? = null,
    val expectedHarvestDate : Date? = null,
    val areaSize : String = "",
    val variety : RiceVariety ?= null,
    val irrigationType : IrrigationType = IrrigationType.GRAVITY_IRRIGATION,
    val soilType : SoilTypes? = null,
    val stage : RiceStage? = null,
    val imageUri : Uri? = null
)