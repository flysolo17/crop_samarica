package com.jmballangca.cropsamarica.data.models.rice_field

import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import java.util.Date
import java.util.concurrent.TimeUnit

enum class RiceStage(name: String) {
    SEEDLING("SEEDLING"),
    TILLERING("TILLERING"),
    STEM_ELONGATION("STEM_ELONGATION"),
    PANICLE_INITIATION("PANICLE_INITIATION"),
    BOOTING("BOOTING"),
    FLOWERING("FLOWERING"),
    MILKING("MILKING"),
    DOUGH("DOUGH"),
    MATURE("MATURE")
}

fun Date.getRiceStage(): RiceStage {
    val currentDate = Date()
    val diffInMillis = currentDate.time - this.time
    val daysSincePlanting = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    return when (daysSincePlanting) {
        in 0..14 -> RiceStage.SEEDLING
        in 15..30 -> RiceStage.TILLERING
        in 31..45 -> RiceStage.STEM_ELONGATION
        in 46..55 -> RiceStage.PANICLE_INITIATION
        in 56..65 -> RiceStage.BOOTING
        in 66..75 -> RiceStage.FLOWERING
        in 76..90 -> RiceStage.MILKING
        in 91..110 -> RiceStage.DOUGH
        else -> RiceStage.SEEDLING
    }
}


fun RiceStage.getHarvestDate(
    variety: RiceVariety
): Date {
    val maturityRange = variety.maturityDays
    val daysToMaturity = maturityRange.min()
    val harvestDate = Date()
    harvestDate.time += TimeUnit.DAYS.toMillis(daysToMaturity.toLong())
    return harvestDate
}
