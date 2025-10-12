package com.jmballangca.cropsamarica.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme

@Composable
fun RegisterPage(
    modifier: Modifier = Modifier,
    onGoogleRegister: () -> Unit = {},
    isLoading: Boolean = false,
    onRegister: (name: String, email: String, password: String) -> Unit = { _, _, _ -> }
) {
    var registerForm by remember {
        mutableStateOf(RegisterFormGroup())
    }
    val name = registerForm.get("name")
    val email = registerForm.get("email")
    val password = registerForm.get("password")

    val confirmPassword = registerForm.get("confirmPassword")
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(
                state = rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            value = name?.value.orEmpty(),
            onValueChange = {
                registerForm.set("name", it)
            },
            placeholder = { Text(stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(

            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Name"
                )
            },

            isError = name?.dirty == true && !name.valid,
            supportingText = {
                if (name?.dirty == true && !name.valid) {
                    Text(
                        name.firstError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }

        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            value = email?.value.orEmpty(),
            onValueChange = {
                registerForm.set("email", it)
            },
            placeholder = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            colors = TextFieldDefaults.colors(

            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            isError = email?.dirty == true && !email.valid,
            supportingText = {
                if (email?.dirty == true && !email.valid) {
                    Text(
                        email.firstError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            value = password?.value.orEmpty(),
            onValueChange = {
                registerForm.set("password", it)
            },
            placeholder = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                 val visibilityIcon = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            supportingText = {
                if (password?.dirty == true && !password.valid) {
                    Text(
                        password.firstError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }

            },
            singleLine = true,
            isError = password?.dirty == true && !password.valid
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(

            ),
            value = confirmPassword?.value.orEmpty(),
            onValueChange = { registerForm.set("confirmPassword", it) },
            placeholder = { Text(stringResource(R.string.confirm_password)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirm Password"
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val visibilityIcon = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            isError = confirmPassword?.dirty == true && !confirmPassword.valid,
            supportingText = {
                if (confirmPassword?.dirty == true && !confirmPassword.valid) {
                    Text(
                        confirmPassword.firstError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            singleLine = true
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading && registerForm.valid,
            onClick = {
                registerForm.validate()
                if (!registerForm.valid) return@Button
                onRegister(
                    name?.value.orEmpty(),
                    email?.value.orEmpty(),
                    password?.value.orEmpty()
                )
            },
            ) {
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        stringResource(R.string.register), style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            onClick = onGoogleRegister
        ) {
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    stringResource(R.string.continue_with_google), style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}


@Preview
@Composable
private fun RegisterPagePrev() {
    CropSamaricaTheme {
        RegisterPage()
    }
}