package com.jmballangca.cropsamarica.domain.models

data class RecommendationResult(
    val recommendations: List<Recommendation>,
    val riceFieldId : String,
)
