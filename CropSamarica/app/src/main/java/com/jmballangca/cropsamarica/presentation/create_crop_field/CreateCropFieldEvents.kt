package com.jmballangca.cropsamarica.presentation.create_crop_field

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.jmballangca.cropsamarica.data.models.rice_field.IrrigationType
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypes
import java.util.Date


sealed interface CreateCropFieldEvents {
    data object Submit : CreateCropFieldEvents
    data class OnCreateTask(
        val tasks : List<Task>
    ) : CreateCropFieldEvents


    object CropForm {
        data class Name(
            val name: String
        ) : CreateCropFieldEvents
        data class Location(
            val location: String
        ) : CreateCropFieldEvents
        data class SoilType(
            val soilType: SoilTypes
        ) : CreateCropFieldEvents
        data class Area(
            val area: String
        ) : CreateCropFieldEvents
        data class PlantedDate(
            val plantedDate: Date
        ) : CreateCropFieldEvents
        data class Variety(
            val variety: RiceVariety
        ) : CreateCropFieldEvents

        data class Irrigation(
            val irrigation: IrrigationType
        ) : CreateCropFieldEvents

        data class OnImageSelected(
            val uri: Uri
        ) : CreateCropFieldEvents

    }
}