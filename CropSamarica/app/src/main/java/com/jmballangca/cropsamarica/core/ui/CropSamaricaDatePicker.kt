package com.jmballangca.cropsamarica.core.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import com.jmballangca.cropsamarica.presentation.common.DatePickerModal
import com.jmballangca.cropsamarica.presentation.common.convertMillisToDate
import com.jmballangca.cropsamarica.presentation.common.toDate
import java.util.Date

@Composable
fun CropSamaricaDatePicker(
    modifier: Modifier = Modifier,
    title : String,
    minDate : Date? = null,
    selected: Date? = null,
    onSelected: (Date) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(selected?.time) }
    var showModal by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = selectedDate?.convertMillisToDate() ?: "",
        textStyle =MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold
        ),
        onValueChange = {},
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(),
        label = { Text(title,) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select Date"
            )
        },

        placeholder = { Text("Select Date") },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {

                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            minDate = minDate,
            onDateSelected = {
                if (it == null) return@DatePickerModal
                selectedDate = it
                onSelected(it.toDate())
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}
