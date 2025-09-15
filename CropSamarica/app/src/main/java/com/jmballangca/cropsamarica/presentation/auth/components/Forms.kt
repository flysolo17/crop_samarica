package com.jmballangca.cropsamarica.presentation.auth.components

import com.jmballangca.formbuilder.FormBuilder
import com.jmballangca.formbuilder.FormControl
import com.jmballangca.formbuilder.Validator

class MatchPasswordValidator(
    private val passwordProvider: () -> String,
    override val message: String = "Passwords do not match."
) : Validator() {

    override fun isValid(value: String): Boolean {
        return value == passwordProvider()
    }
}


class RegisterFormGroup : FormBuilder(
    controls = run {
        val passwordControl = FormControl("", listOf(
            Validator.Required(),
            Validator.MinLength(6),
        ))
        val confirmPasswordValidator = MatchPasswordValidator(
            passwordProvider = { passwordControl.value }
        )
        mapOf(
            "name" to FormControl("", listOf(Validator.Required())),
            "email" to FormControl("", listOf(Validator.Required(), Validator.Email())),
            "password" to passwordControl,
            "confirmPassword" to FormControl("", listOf(Validator.Required(), confirmPasswordValidator)),
        )
    }
)

class LoginFormGroup : FormBuilder(
    controls = mapOf(
        "email" to FormControl("", listOf(Validator.Required(), Validator.Email())),
        "password" to FormControl("", listOf(Validator.Required())),
    )
)