package com.jmballangca.cropsamarica.core.utils

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStatus

fun Context.showToast(message: String, duration: Int = android.widget.Toast.LENGTH_SHORT) {
    android.widget.Toast.makeText(this, message, duration).show()
}
fun RiceStatus.icon(): ImageVector {
    return when (this) {
        RiceStatus.EXCELLENT,
        RiceStatus.GOOD -> Icons.Default.CheckCircle
        RiceStatus.NEEDS_ATTENTION -> Icons.Default.Warning
        RiceStatus.CRITICAL -> Icons.Default.Warning
    }
}

fun RiceStatus.color(): Color {
    return when (this) {
        RiceStatus.EXCELLENT , RiceStatus.GOOD-> Color(0xFF4CAF50) // Green
        RiceStatus.NEEDS_ATTENTION -> Color(0xFFFFC107) // Amber
        RiceStatus.CRITICAL -> Color(0xFFF44336)  // Red
    }
}

