package com.jmballangca.cropsamarica.domain.models





data class Developers(
    val id : String = "",
    val name : String = "",
    val email : String = "",
    val profile : String = "",
    val roles: List<String> = emptyList(),
)



enum class DeveloperRoles(val displayName: String) {
    ANDROID_DEVELOPER("Android Developer"),
    WEB_DEVELOPER("Web Developer"),
    UI_UX_DESIGNER("UI/UX Designer"),
    RESEARCHER("Researcher"),
    DATA_ANALYST("Data Analyst")
}
