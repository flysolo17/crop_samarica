package com.jmballangca.cropsamarica.presentation

import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class RiceFieldDateTimeConverter(
    private val plantedDate: Long,
    private val expectedHarvestDate: Long
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Formatted plantedDate.
     *
     */
    fun displayDate(): String {
        val elapsed = getElapsedDays()
        val total = getTotalDays()
        return "$elapsed of $total days"
    }
    /**
     * Days elapsed since plantedDate until today.
     */
    fun getElapsedDays(): Int {
        val today = System.currentTimeMillis()
        val diff = today - plantedDate
        return TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
    }

    /**
     * Total number of days from plantedDate to expectedHarvestDate.
     */
    fun getTotalDays(): Int {
        val diff = expectedHarvestDate - plantedDate
        return TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
    }

    /**
     * Remaining days until harvest.
     */
    fun getDaysLeft(): Int {
        val today = System.currentTimeMillis()
        val diff = expectedHarvestDate - today
        return TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
    }

    /**
     * Progress percentage of elapsed days vs total days.
     */
    fun getDaysLeftPercentage(): Float {
        val total = getTotalDays()
        if (total == 0) return 0f
        val elapsed = getElapsedDays()
        return (elapsed.toFloat() / total.toFloat()) * 100f
    }

    fun isReadyForNextStage(stage: RiceStage): Boolean {
        val elapsed = getElapsedDays()
        return when (stage) {
            RiceStage.SEEDLING -> elapsed in 0..14          // ~0–14 days
            RiceStage.TILLERING -> elapsed in 15..45        // ~15–45 days
            RiceStage.STEM_ELONGATION -> elapsed in 46..60  // ~46–60 days
            RiceStage.PANICLE_INITIATION -> elapsed in 61..75 // ~61–75 days
            RiceStage.BOOTING -> elapsed in 76..85          // ~76–85 days
            RiceStage.FLOWERING -> elapsed in 86..95        // ~86–95 days
            RiceStage.MILKING -> elapsed in 96..105         // ~96–105 days
            RiceStage.DOUGH -> elapsed in 106..115          // ~106–115 days
            RiceStage.MATURE -> elapsed >= 116              // ≥116 days (harvest-ready)
        }
    }

    fun getDaysLeftBeforeNextStage(stage: RiceStage): String {
        val elapsed = getElapsedDays()
        val nextStageDay = when (stage) {
            RiceStage.SEEDLING -> 15
            RiceStage.TILLERING -> 46
            RiceStage.STEM_ELONGATION -> 61
            RiceStage.PANICLE_INITIATION -> 76
            RiceStage.BOOTING -> 86
            RiceStage.FLOWERING -> 96
            RiceStage.MILKING -> 106
            RiceStage.DOUGH -> 116
            RiceStage.MATURE -> null
        }

        return if (nextStageDay != null) {
            val left = nextStageDay - elapsed
            when {
                left > 1 -> "$left days more before next stage"
                left == 1 -> "1 day more before next stage"
                else -> "Ready for next stage"
            }
        } else {
            "Already at harvest stage"
        }
    }
}