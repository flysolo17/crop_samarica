package com.jmballangca.cropsamarica.presentation.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordBottomSheet(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    onSubmit : (String) -> Unit,
) {
    var open by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var valid by remember { mutableStateOf(true) }
    LaunchedEffect(email) {
        valid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    if (open) {
        ModalBottomSheet(
            onDismissRequest = {
                open = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Enter your email address to reset your password",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.colors(),
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !valid,
                    singleLine = true,
                    supportingText = {
                        if (!valid) {
                            Text("Invalid email address")
                        }
                    }
                )
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Button(
                    onClick = {
                        onSubmit(email)
                        email = ""
                        open = false
                    },
                    enabled = valid && !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        if (isLoading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier.size(24.dp)
                            )
                        } else {
                            Text("Submit")
                        }

                    }
                }
          }
        }
    }
    TextButton(
        onClick = {
            open = !open
        },
        modifier = modifier
    ) {
        Text("Forgot Password")
    }
}


@Preview
@Composable
private fun ForgotPasswordBottomSheetPreview() {
    CropSamaricaTheme {
        ForgotPasswordBottomSheet(
            isLoading = false,
            onSubmit = {}
        )
    }
}