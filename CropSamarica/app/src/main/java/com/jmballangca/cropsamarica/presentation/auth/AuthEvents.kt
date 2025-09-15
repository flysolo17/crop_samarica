package com.jmballangca.cropsamarica.presentation.auth



sealed interface AuthEvents {
    data class Login(val email: String, val password: String) : AuthEvents
    data class Register(val name: String, val email: String, val password: String) : AuthEvents
    data class ForgotPassword(val email: String) : AuthEvents
    data object SignWithGoogle : AuthEvents
    data object RegisterWithGoogle : AuthEvents
}