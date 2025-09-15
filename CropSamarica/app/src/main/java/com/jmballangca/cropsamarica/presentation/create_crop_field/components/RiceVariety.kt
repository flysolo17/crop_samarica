package com.jmballangca.cropsamarica.presentation.create_crop_field.components

import com.google.firebase.firestore.PropertyName


enum class RiceVariety(
    @get:PropertyName("displayName") // Tells Firestore to use this property name for the field
    val displayName: String,
    val environment: EnvironmentType,
    val maturityDays: IntRange,
    val description: String
) {
    // Enum constants should be valid identifiers
    NSIC_RC222(
        displayName = "NSIC Rc 222 (Tubigan 18)",
        environment = EnvironmentType.IRRIGATED,
        maturityDays = 110..115,
        description = "High-yielding, widely grown nationwide, soft eating quality."
    ),
    NSIC_RC160(
        displayName = "NSIC Rc 160 (Tubigan 14)",
        environment = EnvironmentType.IRRIGATED,
        maturityDays = 111..115,
        description = "Early maturing with good eating quality."
    ),
    NSIC_RC216(
        displayName = "NSIC Rc 216 (Tubigan 17)",
        environment = EnvironmentType.IRRIGATED,
        maturityDays = 108..111,
        description = "Early maturing, high yield, good milling recovery."
    ),
    NSIC_RC300(
        displayName = "NSIC Rc 300 (Tubigan 26)",
        environment = EnvironmentType.IRRIGATED,
        maturityDays = 112..116,
        description = "Resistant to pests and diseases."
    ),

    // 🌱 Rainfed Lowland Varieties
    NSIC_RC192(
        displayName = "NSIC Rc 192 (Sahod Ulan 1)",
        environment = EnvironmentType.RAINFED,
        maturityDays = 108..112,
        description = "For rainfed conditions, good grain quality."
    ),
    NSIC_RC272(
        displayName = "NSIC Rc 272 (Sahod Ulan 5)",
        environment = EnvironmentType.RAINFED,
        maturityDays = 110..115,
        description = "Drought-tolerant, high yield potential."
    ),
    NSIC_RC486(
        displayName = "NSIC Rc 486 (Sahod Ulan 12)",
        environment = EnvironmentType.RAINFED,
        maturityDays = 110..116,
        description = "Improved stress tolerance for rainfed areas."
    ),

    // 🏞️ Upland Varieties
    NSIC_RC9(
        displayName = "NSIC Rc 9 (Upland Dinorado)",
        environment = EnvironmentType.UPLAND,
        maturityDays = 115..120,
        description = "Traditional aromatic variety, premium eating quality."
    ),

    // 🌍 Hybrid Rice
    SL_8H(
        displayName = "SL-8H (Super Hybrid)",
        environment = EnvironmentType.HYBRID,
        maturityDays = 115..120,
        description = "Very high yield potential (10–12 tons/ha)."
    ),
    MESTISO_20(
        displayName = "Mestiso 20 (NSIC Rc 204H)",
        environment = EnvironmentType.HYBRID,
        maturityDays = 111..115,
        description = "Hybrid developed by PhilRice."
    ),
    MESTISO_29(
        displayName = "Mestiso 29 (NSIC Rc 300H)",
        environment = EnvironmentType.HYBRID,
        maturityDays = 113..118,
        description = "Hybrid rice with high yield and stress tolerance."
    ),

    // ✍️ Fallback for manual entry
    OTHER(
        displayName = "Other (Manual Input)",
        environment = EnvironmentType.UNKNOWN,
        maturityDays = 100..130, // Example range
        description = "Custom variety entered by user."
    );

    // No changes needed for EnvironmentType unless it's also stored with custom names
    enum class EnvironmentType {
        IRRIGATED, RAINFED, UPLAND, HYBRID, UNKNOWN
    }

    companion object {
        // This function helps find an enum constant by its displayName.
        // Useful if you have the displayName string and need the enum object.
        fun fromDisplayName(displayName: String): RiceVariety? {
            return entries.find { it.displayName == displayName }
        }
    }
}