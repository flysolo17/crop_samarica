package com.jmballangca.cropsamarica.domain.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.ZoneOffset.UTC
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun Date.monthAndDay() : String {
    return try {
        val outputFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        outputFormat.format(this)
    } catch (e: Exception) {
        this.toString()
    }
}

fun String.toDateOnly(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}

fun String.toDay() : String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        "invalid"
    }
}

fun Long.toYmdPht(): String {
    return SimpleDateFormat("yyyy-MM-dd").apply { timeZone = TimeZone.getTimeZone("Asia/Manila") }.format(Date(this))
}


fun Long.displayDate(): String {

    return try {
        val date = Date(this)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        this.toString() // fallback if formatting fails
    }
}

fun Date.toDateOnly(): String {
    return try {
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        outputFormat.format(this)
    } catch (e: Exception) {
        this.toString() // fallback if formatting fails
    }
}


fun String.toProperTime(): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())  // 24-hour input
        val outputFormat =
            SimpleDateFormat("h a", Locale.getDefault())   // 12-hour output with AM/PM
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this // fallback if parsing fails
    }
}