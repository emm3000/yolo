package com.emm.yolo.feature.history

import com.emm.yolo.feature.home.calculateCurrentStreak
import com.emm.yolo.feature.log.Duration
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

fun calculateWeekStreak(sessionUis: List<EnglishSessionUi>): Int {
     val zoneId = ZoneId.systemDefault()

     val startOfWeek = LocalDate.now(zoneId)
         .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
         .atStartOfDay(zoneId)
         .toInstant()
         .toEpochMilli()

     val endOfWeek = LocalDate.now(zoneId)
         .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
         .atTime(23, 59, 59)
         .atZone(zoneId)
         .toInstant()
         .toEpochMilli()

     return sessionUis.count { session -> session.sessionDateInMillis in startOfWeek..endOfWeek }
 }

fun calculateStreak(sessionUis: List<EnglishSessionUi>): String {
    val sessionDates: List<Long> = sessionUis.map(EnglishSessionUi::sessionDateInMillis)
    return calculateCurrentStreak(sessionDates).toString()
}

fun calculateAvg(sessionUis: List<EnglishSessionUi>): Duration? {
    return sessionUis
        .groupingBy(EnglishSessionUi::duration)
        .eachCount()
        .maxByOrNull(Map.Entry<Duration, Int>::value)
        ?.key
}