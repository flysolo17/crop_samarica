package com.jmballangca.cropsamarica.presentation.profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.presentation.profile.ProfileButtons
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    currentName: String,
    onSaveChanges: (name : String, result : (UIState<String>) -> Unit) -> Unit,
) {
    var name by remember { mutableStateOf(currentName) }
    var open by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    if (open) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            onDismissRequest = {
                open = false
            },
        ) {
            Column(
                modifier = modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    "Edit Profile",

                )
                OutlinedTextField(
                    value = name,
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.colors(),
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Name")
                    }
                )
                Button(
                    enabled = !loading,
                    onClick = {
                        onSaveChanges(name) {
                            when(it) {
                                is UIState.Error -> {
                                    context.showToast(it.message)
                                    loading = false
                                }
                                UIState.Loading -> {
                                    loading = true
                                }
                                is UIState.Success<String> -> {
                                    context.showToast(it.data)
                                    loading = false
                                    open = false
                                }
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        if (loading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(text = "Save Changes")
                        }
                    }
                }

            }
        }
    }
    ProfileButtons(
        icon = Icons.Filled.Edit,
        title = "Edit Profile",
        onClick = {
            open = !open
        }
    )
}

@Preview
@Composable
private fun EditProfilePrev() {
    CropSamaricaTheme {
        EditProfile(
            currentName = "Juan Dela Cruz",
            onSaveChanges = { name, result ->
                result(UIState.Success("Success"))
            }
        )
    }
}