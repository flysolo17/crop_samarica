package com.jmballangca.cropsamarica.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit



fun String.toCamelCase(): String {
    return this
        .lowercase()
        .split(" ", "_", "-", ".")
        .filter { it.isNotBlank() }
        .mapIndexed { index, word ->
            if (index == 0) word
            else word.replaceFirstChar { it.uppercase() }
        }
        .joinToString("")
}


fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Bitmap.CompressFormat.WEBP_LOSSY
    } else {
        Bitmap.CompressFormat.WEBP
    }
    this.compress(format, 75, stream)
    return stream.toByteArray()
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}