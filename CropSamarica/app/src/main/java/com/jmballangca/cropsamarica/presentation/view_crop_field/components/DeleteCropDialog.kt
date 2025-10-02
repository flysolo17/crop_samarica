package com.jmballangca.cropsamarica.presentation.view_crop_field.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog

@Composable
fun DeleteCropDialog(
    modifier: Modifier = Modifier,
    name: String,
    onConfirm: () -> Unit
) {
    var open by remember { mutableStateOf(false) }

    if (open) {
        AlertDialog(
            onDismissRequest = { open = false },
            title = {
                Text(text = "Delete Crop")
            },
            text = {
                Text(
                    text = "Are you sure you want to permanently delete \"$name\"? " +
                            "This action cannot be undone.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = {
                        onConfirm()
                        open = false
                    }
                ) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { open = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    IconButton(
        onClick = { open = true },
        colors = IconButtonDefaults.iconButtonColors(

            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete"
        )
    }
}
