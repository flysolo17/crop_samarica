package com.jmballangca.cropsamarica.presentation.create_crop_field.components




enum class Suitability {
    HIGH,   // Best for rice – very suitable
    MEDIUM, // Can support rice but needs careful management
    LOW     // Challenging for rice – requires extra soil treatment/fertilizer
}

enum class SoilTypes(
    val label: String,
    val description: String,
    val suitability: Suitability
) {
    CLAY(
        "Clay",
        "Heavy soil that holds water well. Best for lowland irrigated rice.",
        Suitability.HIGH
    ),
    CLAY_LOAM(
        "Clay Loam",
        "Balanced mix of clay and loam. Suitable for both irrigated and rainfed rice.",
        Suitability.HIGH
    ),
    SILTY_CLAY(
        "Silty Clay / Silty Loam",
        "Fertile soil with high silt content. Retains nutrients and water well.",
        Suitability.HIGH
    ),
    SANDY_LOAM(
        "Sandy Loam",
        "Light soil with good drainage. Requires frequent irrigation and fertilizer.",
        Suitability.MEDIUM
    ),
    LOAM(
        "Loam",
        "Ideal balance of sand, silt, and clay. Fertile and suitable for most rice systems.",
        Suitability.HIGH
    ),
    PEATY(
        "Peaty",
        "Organic-rich but acidic soil. Found in swampy areas, may need liming.",
        Suitability.LOW
    ),
    ALLUVIAL(
        "Alluvial",
        "Fertile soil from river deposits. Common in floodplains and valleys.",
        Suitability.HIGH
    ),
    VOLCANIC_SOIL(
        "Volcanic Soil",
        "Mineral-rich soil near volcanic areas. Fertile but can be acidic.",
        Suitability.MEDIUM
    );
}
