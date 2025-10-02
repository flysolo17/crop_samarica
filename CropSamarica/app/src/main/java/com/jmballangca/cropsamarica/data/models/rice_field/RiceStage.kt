package com.jmballangca.cropsamarica.data.models.rice_field

import androidx.annotation.DrawableRes
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import java.util.Date
import java.util.concurrent.TimeUnit

enum class RiceStage(
    val displayName: String,
    @DrawableRes  val icon : Int
) {
    SEEDLING(
        displayName = "SEEDLING",
        icon = R.drawable.seedling
    ),
    TILLERING(
        displayName ="TILLERING",
        icon = R.drawable.tillering
    ),
    STEM_ELONGATION(
        displayName ="STEM_ELONGATION",
        icon = R.drawable.stem_elongation
    ),
    PANICLE_INITIATION(
        displayName = "PANICLE_INITIATION",
        icon = R.drawable.panicle_initiation
    ),
    BOOTING(
        displayName="BOOTING",
        icon = R.drawable.booting
    ),
    FLOWERING(displayName ="FLOWERING"
        ,icon = R.drawable.flowering),
    MILKING("MILKING",icon = R.drawable.milking),
    DOUGH("DOUGH",icon = R.drawable.dough),
    MATURE("MATURE",icon = R.drawable.mature)
}

fun RiceStage.getIcon() : Int {
    return when (this) {
        RiceStage.SEEDLING -> R.drawable.seedling
        RiceStage.TILLERING -> R.drawable.tillering
        RiceStage.STEM_ELONGATION -> R.drawable.stem_elongation
        RiceStage.PANICLE_INITIATION -> R.drawable.panicle_initiation
        RiceStage.BOOTING -> R.drawable.booting
        RiceStage.FLOWERING -> R.drawable.flowering
        RiceStage.MILKING -> R.drawable.milking
        RiceStage.DOUGH -> R.drawable.dough
        RiceStage.MATURE -> R.drawable.mature
    }
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
