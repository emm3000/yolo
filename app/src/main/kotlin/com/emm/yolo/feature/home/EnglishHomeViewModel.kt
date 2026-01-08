package com.emm.yolo.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.EnglishSession
import com.emm.yolo.data.LogRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class EnglishHomeViewModel(private val logRepository: LogRepository) : ViewModel() {

    var state by mutableStateOf(EnglishHomeUiState())
        private set

    init {
        fetchHomeData()
    }

    fun fetchHomeData() = viewModelScope.launch {
        logRepository.selectAllSessions()
            .map { englishSessions ->
                val sessionDates: List<Long> = englishSessions.map(EnglishSession::sessionDate)
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