package com.jmballangca.cropsamarica.presentation.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    modifier: Modifier = Modifier,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    val languages =listOf(
        Pair("en","English"),
        Pair("tl","Tagalog")
    )
    val selectedLanguage = languages.find { it.first == selectedLanguage }?.second ?: "English"
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language"
                )
            },
            readOnly = true,
            singleLine = true,
            colors = TextFieldDefaults.colors(),
            shape = MaterialTheme.shapes.medium,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            languages.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it.second)
                    },
                    onClick = {
                        onLanguageSelected(it.first)
                        expanded = false
                    }
                )
            }
        }
    }
}