package com.jmballangca.cropsamarica.presentation.create_crop_field.components

import com.jmballangca.formbuilder.FormBuilder
import com.jmballangca.formbuilder.FormControl


class CreateCropForm : FormBuilder(
    controls = run {
        mapOf(
            "name" to FormControl(""),
            "location" to FormControl(""),
            "soilType" to FormControl(""),
            "area" to FormControl(""),
            "plantedDate" to FormControl(""),
            "variety" to FormControl(""),
            "lastFertilizer" to FormControl(""),
            "irrigation" to FormControl(""),
        )
    }
)