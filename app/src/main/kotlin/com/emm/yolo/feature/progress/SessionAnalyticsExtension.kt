package com.emm.yolo.feature.progress

import com.emm.yolo.feature.history.EnglishSessionUi
import com.emm.yolo.feature.log.PracticeType
import java.time.LocalDate

fun List<EnglishSessionUi>.practiceTypeProgress(): Map<PracticeType, Float> {
    val total = size.toFloat().takeIf { it > 0 } ?: 1f

    val counts: Map<PracticeType, Int> = this
        .groupingBy(EnglishSessionUi::practiceType)
        .eachCount()

    return PracticeType.entries.associateWith { type ->
        val count = counts[type] ?: 0
        count / total
    }
}

fun List<EnglishSessionUi>.groupByDay(): Map<LocalDate, Long> {
    return this
        .groupingBy(EnglishSessionUi::sessionDate)
        .fold(0L) { acc, session -> acc + session.duration.minutes }
}

fun List<EnglishSessionUi>.mapByDay(): Map<LocalDate, Long> {
    return associate { it.sessionDate to it.duration.minutes }
}

fun buildLast14DaysFromSessions(
    sessions: List<EnglishSessionUi>,
    maxMinutes: Long = 30,
): List<Float> {
    val today: LocalDate = LocalDate.now()
    val minutesByDay: Map<LocalDate, Long> = sessions.mapByDay()

    return (13 downTo 0).map { offset ->
        val day: LocalDate = today.minusDays(offset.toLong())
        val minutes: Long = minutesByDay[day] ?: 0L

        (minutes.toFloat() / maxMinutes).coerceIn(0f, 1f)
    }
}

fun List<EnglishSessionUi>.totalMinutesLast14Days(): Long {
    val today: LocalDate = LocalDate.now()
    val startDay: LocalDate = today.minusDays(13)

    return this
        .filter { it.sessionDate in startDay..today }
        .sumOf { it.duration.minutes }
}

fun Long.toHoursAndMinutes(): String {
    val hours: Long = this / 60
    val minutes: Long = this % 60
    return "${hours}h ${minutes}m"
}