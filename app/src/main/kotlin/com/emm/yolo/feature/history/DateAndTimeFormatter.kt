package com.emm.yolo.feature.history

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

fun LocalDate.toFriendlyDate(today: LocalDate = LocalDate.now()): String {
    val daysBetween = ChronoUnit.DAYS.between(this, today)

    return when {
        daysBetween == 0L -> "Today"
        daysBetween == 1L -> "Yesterday"
        daysBetween in 2..6 -> this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        this.year == today.year -> this.format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH))
        else -> this.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH))
    }
}

fun LocalDateTime.toTimeAgo(now: LocalDateTime = LocalDateTime.now()): String {
    val seconds = ChronoUnit.SECONDS.between(this, now)
    val minutes = ChronoUnit.MINUTES.between(this, now)
    val hours = ChronoUnit.HOURS.between(this, now)
    val days = ChronoUnit.DAYS.between(this, now)

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days == 1L -> "Yesterday at ${this.format(timeFormatter)}"
        days < 7 -> "${this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} at ${this.format(timeFormatter)}"
        else -> this.format(dateTimeFormatter)
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)

private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d 'at' h:mm a", Locale.ENGLISH)

fun LocalTime.toFriendlyTime(): String {
    return this.format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
}