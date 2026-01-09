package com.emm.yolo.feature.share

import com.emm.yolo.SelectAllWithAudioInfo
import com.emm.yolo.feature.history.EnglishSessionUi
import com.emm.yolo.feature.log.PracticeType
import com.emm.yolo.feature.log.toDuration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

fun mapToUi(sessions: List<SelectAllWithAudioInfo>): List<EnglishSessionUi> = sessions.map { session ->
    EnglishSessionUi(
        id = session.id,
        sessionDate = Instant
            .ofEpochMilli(session.sessionDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        sessionHour = LocalTime.ofSecondOfDay(session.sessionHour),
        sessionDateInMillis = session.sessionDate,
        duration = session.minutesPracticed.toDuration(),
        practiceType = PracticeType.valueOf(session.practiceType),
        confidenceLevel = session.confidenceLevel,
        discomfortLevel = session.discomfortLevel,
        notes = session.notes,
        createdAt = session.createdAt,
        updatedAt = session.updatedAt,
        hasAudio = session.hasAudio,
    )
}