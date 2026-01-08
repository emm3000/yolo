package com.emm.yolo

import com.emm.yolo.data.InsertSession
import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.PracticeType
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun testDate(): List<InsertSession> {
    val date: LocalDate = LocalDate.now()

    return (1L..1000L).map {
        InsertSession(
            sessionDate = date.minusDays(it)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            sessionHour = LocalTime.now().toSecondOfDay().toLong(),
            minutesPracticed = Duration.FiveMinutes,
            practiceType = PracticeType.Speaking,
            confidenceLevel = 7085,
            discomfortLevel = 4987,
            notes = "atqui $it"
        )
    }
}