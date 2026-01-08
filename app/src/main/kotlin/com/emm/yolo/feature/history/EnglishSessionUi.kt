package com.emm.yolo.feature.history

import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.PracticeType
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class EnglishSessionUi(
    val id: Long,
    val sessionDate: LocalDate,
    val sessionHour: LocalTime,
    val minutesPracticed: Duration,
    val practiceType: PracticeType,
    val confidenceLevel: Long,
    val discomfortLevel: Long,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
) {

    val formattedSessionDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return formatter.format(sessionDate)
        }

    val formattedSessionHour: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            return sessionHour.format(formatter)
        }
}