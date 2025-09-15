package com.jmballangca.cropsamarica.domain.models

import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage


data class Recommendation(
    val id: String = "",
    val fieldId: String = "",
    val title: String = "",
    var stage: RiceStage = RiceStage.TILLERING,
    val details: String = "",
)

val RECOMMENDATIONS = listOf(
    Recommendation(
        id = "rec1",
        title = "Soil testing",
        stage = RiceStage.TILLERING,
        details = "Conduct soil test to determine nutrient requirements.",

    ),
    Recommendation(
        id = "rec2",
        title = "Fertilizer management",
        stage = RiceStage.TILLERING,
        details = "Apply balanced NPK fertilizer based on soil test results.",
    ),

    Recommendation(
        id = "rec3",
        title = "Irrigation scheduling",
        stage = RiceStage.TILLERING,
        details = "Ensure adequate but not excessive water supply.",
    ),

    Recommendation(
        id = "rec4",
        title = "Weed control",
        stage = RiceStage.TILLERING,
        details = "Implement mechanical or chemical weed control.",
    ),
    Recommendation(
        id = "rec5",
        title = "Pest and disease monitoring",
        stage = RiceStage.TILLERING,
        details = "Inspect regularly and apply IPM practices if needed.",
    ),
    Recommendation(
        id = "rec6",
        title = "Harvest planning",
        stage = RiceStage.TILLERING,
        details = "Prepare equipment and schedule harvest when crop reaches maturity.",
    )
)
