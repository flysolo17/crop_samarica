package com.jmballangca.cropsamarica.data.models.rice_field

import androidx.annotation.DrawableRes
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import java.util.Date
import java.util.concurrent.TimeUnit
enum class RiceStage(
    val displayName: String,
    @DrawableRes val icon: Int,
    val description: String,
    val thingsToDo: List<String> = emptyList(),
    val thingsToLookOutFor: List<String> = emptyList(),
    val daysRange: IntRange
) {
    SEEDLING(
        displayName = "Seedling",
        icon = R.drawable.seedling,
        description = "The first stage after germination when young rice plants develop.",
        thingsToDo = listOf(
            "Ensure proper water management (shallow flooding or moist soil).",
            "Apply basal fertilizer to encourage root and leaf development.",
            "Weed regularly to reduce competition."
        ),
        thingsToLookOutFor = listOf(
            "Seedling blight and damping-off diseases.",
            "Nutrient deficiencies such as yellowing leaves.",
            "Pest attacks like leafhoppers and stem borers."
        ),
        daysRange = 0..14
    ),
    TILLERING(
        displayName = "Tillering",
        icon = R.drawable.tillering,
        description = "The rice plant produces multiple shoots (tillers) from the base, which help determine yield potential.",
        thingsToDo = listOf(
            "Apply adequate nitrogen fertilizer to promote strong tiller growth.",
            "Maintain water depth of 2–5 cm to encourage tillering.",
            "Control weeds to reduce competition."
        ),
        thingsToLookOutFor = listOf(
            "Overcrowding of tillers, which can reduce grain quality.",
            "Stem borers and leaf folder pests."
        ),
        daysRange = 15..30
    ),
    STEM_ELONGATION(
        displayName = "Stem Elongation",
        icon = R.drawable.stem_elongation,
        description = "The main stem and tillers grow taller, preparing the plant for panicle development.",
        thingsToDo = listOf(
            "Apply topdressing fertilizer (especially nitrogen and potassium).",
            "Maintain 5–10 cm water depth to support stem growth."
        ),
        thingsToLookOutFor = listOf(
            "Lodging (plants falling due to weak stems).",
            "Stem borer infestations and sheath blight disease."
        ),
        daysRange = 31..45
    ),
    PANICLE_INITIATION(
        displayName = "Panicle Initiation",
        icon = R.drawable.panicle_initiation,
        description = "The plant begins forming panicles (future grain heads), which is a critical stage for yield.",
        thingsToDo = listOf(
            "Apply split fertilizer application, focusing on phosphorus and potassium.",
            "Maintain consistent water levels for steady growth."
        ),
        thingsToLookOutFor = listOf(
            "Pests damaging developing panicles.",
            "Nutrient deficiency symptoms such as purpling leaves (phosphorus deficiency)."
        ),
        daysRange = 46..55
    ),
    BOOTING(
        displayName = "Booting",
        icon = R.drawable.booting,
        description = "The panicle swells inside the flag leaf sheath, preparing to emerge.",
        thingsToDo = listOf(
            "Apply protective fungicides if needed.",
            "Ensure consistent irrigation and avoid drought stress."
        ),
        thingsToLookOutFor = listOf(
            "False smut disease and panicle pests.",
            "Water stress that reduces grain filling."
        ),
        daysRange = 56..65
    ),
    FLOWERING(
        displayName = "Flowering",
        icon = R.drawable.flowering,
        description = "Rice plants bloom and pollination occurs. This is the most sensitive stage for yield.",
        thingsToDo = listOf(
            "Maintain shallow flooding to avoid water stress.",
            "Protect flowers from pests such as rice bugs."
        ),
        thingsToLookOutFor = listOf(
            "High temperatures or drought causing spikelet sterility.",
            "Pest damage reducing pollination success."
        ),
        daysRange = 66..75
    ),
    MILKING(
        displayName = "Milking",
        icon = R.drawable.milking,
        description = "Grains contain a milky fluid as starch begins to form inside the kernels.",
        thingsToDo = listOf(
            "Maintain sufficient irrigation to support grain filling.",
            "Apply potassium fertilizer if deficiency is visible."
        ),
        thingsToLookOutFor = listOf(
            "Grain-sucking insects such as rice bugs.",
            "Drought stress leading to incomplete grain filling."
        ),
        daysRange = 76..90
    ),
    DOUGH(
        displayName = "Dough",
        icon = R.drawable.dough,
        description = "Grains harden from milky to soft dough consistency as starch solidifies.",
        thingsToDo = listOf(
            "Keep soil moist but avoid deep flooding.",
            "Monitor for late-season pests and diseases."
        ),
        thingsToLookOutFor = listOf(
            "Sheath blight disease.",
            "Late rice bug infestations.",
            "Lodging caused by heavy panicles and weak stems."
        ),
        daysRange = 91..110
    ),
    MATURE(
        displayName = "Mature",
        icon = R.drawable.mature,
        description = "Grains turn hard and golden, signaling that the crop is ready for harvest.",
        thingsToDo = listOf(
            "Drain fields 7–10 days before harvest to ease harvesting.",
            "Harvest when 80–90% of grains are mature."
        ),
        thingsToLookOutFor = listOf(
            "Overdrying and shattering if harvest is delayed.",
            "Bird damage and rodent attacks."
        ),
        daysRange = 111..120
    )
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
