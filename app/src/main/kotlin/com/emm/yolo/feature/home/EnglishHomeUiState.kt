package com.emm.yolo.feature.home

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class EnglishHomeUiState(
    val hasPracticed: Boolean = false,
    val streak: Int = 0,
    val totalSessions: Int = 0,
    val monthSessions: Int = 0,
    val currentDate: LocalDate = LocalDate.now(),
) {

    val formattedDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH)
            return currentDate.format(formatter)
        }
}