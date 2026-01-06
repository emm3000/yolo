package com.emm.yolo.presentation.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.EnglishSession
import com.emm.yolo.data.Repository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class EnglishHomeViewModel(private val repository: Repository) : ViewModel() {

    var state by mutableStateOf(EnglishHomeUiState())
        private set

    init {
        fetchHomeData()
    }

    fun fetchHomeData() = viewModelScope.launch {
        repository.selectAllSessions()
            .map { englishSessions ->
                val sessionDates: List<Long> = englishSessions.map(EnglishSession::session_date)
                val today: Long = LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
                EnglishHomeUiState(
                    hasPracticed = today == sessionDates.firstOrNull(),
                    streak = calculateCurrentStreak(sessionDates),
                    totalSessions = englishSessions.count(),
                    monthSessions = calculateActiveMonths(sessionDates)
                )
            }
            .collect {
                state = it
            }
    }
}

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
        .map { date ->
            date.year * 100 + date.monthValue
        }
        .distinct()
        .count()
}