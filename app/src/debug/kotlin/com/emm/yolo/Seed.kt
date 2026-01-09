package com.emm.yolo

import com.emm.yolo.data.LogRepository
import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.InsertSession
import com.emm.yolo.feature.log.PracticeType
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

suspend fun seed(repository: LogRepository) {
    val date: LocalDate = LocalDate.now()

    (1L..1000L).forEach {
        val session = InsertSession(
            sessionDate = date.minusDays(it)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            sessionHour = LocalTime.now().toSecondOfDay().toLong(),
            minutesPracticed = Duration.entries.random(),
            practiceType = PracticeType.entries.random(),
            confidenceLevel = 7085,
            discomfortLevel = 4987,
            notes = "atqui $it"
        )
        repository.insertSession(session)
    }
}