package com.jmballangca.cropsamarica.data.models.rice_field

import com.jmballangca.cropsamarica.domain.models.Recommendation
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypes
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.util.Date

data class RiceField(
    var id: String = "",
    var uid: String = "",
    val name: String = "",
    val stage: RiceStage = RiceStage.TILLERING,
    var location: String = "",
    val plantedDate: Long = System.currentTimeMillis(), // epoch millis (UTC)
    val expectedHarvestDate: Long? = null,             // epoch millis (UTC)
    val variety: RiceVariety = RiceVariety.NSIC_RC9,
    val status: RiceStatus = RiceStatus.EXCELLENT,
    val areaSize: Double = 0.0,
    val irrigationType: IrrigationType = IrrigationType.NONE,
    val soilType : SoilTypes = SoilTypes.CLAY,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
)


fun Map<String, JsonElement>.asRiceField(): RiceField {
    return RiceField(
        id = this["id"]?.jsonPrimitive?.content ?: "",
        name = this["name"]?.jsonPrimitive?.content ?: "",
        stage = this["stage"]?.jsonPrimitive?.contentOrNull?.let {
            runCatching { RiceStage.valueOf(it) }.getOrDefault(RiceStage.TILLERING)
        } ?: RiceStage.TILLERING,
        location = this["location"]?.jsonPrimitive?.content ?: "",
        plantedDate = this["planted_date"]?.jsonPrimitive?.longOrNull ?: System.currentTimeMillis(),
        expectedHarvestDate = this["expected_harvest_date"]?.jsonPrimitive?.longOrNull ?: System.currentTimeMillis(),
        variety = (this["variety"]?.jsonPrimitive?.content ?: RiceVariety.NSIC_RC9) as RiceVariety,
        soilType = this["soil_type"]?.jsonPrimitive?.contentOrNull?.let {
            runCatching { SoilTypes.valueOf(it) }.getOrDefault(SoilTypes.CLAY)
        } ?: SoilTypes.CLAY,
        status = this["status"]?.jsonPrimitive?.contentOrNull?.let {
            runCatching { RiceStatus.valueOf(it) }.getOrDefault(RiceStatus.EXCELLENT)
        } ?: RiceStatus.EXCELLENT,
        areaSize = this["area"]?.jsonPrimitive?.doubleOrNull ?: 0.0,
        createdAt = Date(),
        updatedAt = Date(),
        irrigationType = this["irrigation"]?.jsonPrimitive?.contentOrNull?.let {
            runCatching { IrrigationType.valueOf(it) }.getOrDefault(IrrigationType.NONE)
        } ?: IrrigationType.NONE,
    )
}

fun JsonObject.asRecommendation(): Recommendation {
    return Recommendation(
        stage = this["stage"]?.jsonPrimitive?.contentOrNull?.let {
            runCatching { RiceStage.valueOf(it) }.getOrDefault(RiceStage.TILLERING)
        } ?: RiceStage.TILLERING,
        title = this["title"]?.jsonPrimitive?.content ?: "General Recommendation",
        details = this["details"]?.jsonPrimitive?.content ?: ""
    )
}
