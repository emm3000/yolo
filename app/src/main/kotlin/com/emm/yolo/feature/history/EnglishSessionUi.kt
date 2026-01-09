package com.emm.yolo.feature.history

import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.PracticeType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class EnglishSessionUi(
    val id: Long,
    val sessionDate: LocalDate,
    val sessionDateInMillis: Long,
    val sessionHour: LocalTime,
    val duration: Duration,
    val practiceType: PracticeType,
    val confidenceLevel: Long,
    val discomfortLevel: Long,
    val notes: String?,
    val hasAudio: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
) {

    val formattedSessionDate: String
        get() = sessionDate.toFriendlyDate()

    val formattedSessionHour: String
        get() = LocalDateTime.of(sessionDate, sessionHour).toTimeAgo()
}