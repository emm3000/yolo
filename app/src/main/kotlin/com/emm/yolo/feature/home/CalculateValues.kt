package com.emm.yolo.feature.home

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun calculateCurrentStreak(sessionDates: List<Long>): Int {
    if (sessionDates.isEmpty()) return 0

    val dateSet = sessionDates.toSet()

    var streak = 0
    var currentDate = LocalDate.now()

    while (true) {
        val epoch = currentDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        if (epoch in dateSet) {
            streak++
            currentDate = currentDate.minusDays(1)
        } else {
            break
        }
    }

    return streak
}

fun calculateActiveMonths(sessionDates: List<Long>): Int {
    return sessionDates
        .map {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        .map { date -> date.year * 100 + date.monthValue }
        .distinct()
        .count()
}