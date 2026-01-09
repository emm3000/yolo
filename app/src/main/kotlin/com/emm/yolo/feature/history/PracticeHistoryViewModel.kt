package com.emm.yolo.feature.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.data.LogRepository
import com.emm.yolo.feature.share.mapToUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class PracticeHistoryViewModel(private val logRepository: LogRepository) : ViewModel() {

    var state by mutableStateOf(PracticeHistoryUiState())
        private set

    fun action(action: PracticeHistoryAction) = when (action) {
        PracticeHistoryAction.AllPracticeTypes -> {
            state = state.copy(selectedPracticeType = null, filteredSessions = state.sessions)
        }

        is PracticeHistoryAction.PickPracticeType -> {
            val filteredSessions: List<EnglishSessionUi> = state.sessions.filter { it.practiceType == action.practiceType }
            state = state.copy(selectedPracticeType = action.practiceType, filteredSessions = filteredSessions)
        }
    }

    init {
        fetchAll()
    }

    fun fetchAll() {
        logRepository.selectAllSessionsWithAudio()
            .map(::mapToUi)
            .onEach { sessionUis ->
                val filteredSession: List<EnglishSessionUi> = filteringSessions(sessionUis)
                state = state.copy(
                    sessions = sessionUis,
                    filteredSessions = filteredSession,
                    streak = calculateStreak(sessionUis),
                    avgTime = calculateAvg(sessionUis),
                    thisWeek = calculateWeekStreak(sessionUis).toString()
                )
            }
            .launchIn(viewModelScope)
    }

    private fun filteringSessions(sessionUis: List<EnglishSessionUi>): List<EnglishSessionUi> {
        val isAllSelected: Boolean = state.selectedPracticeType == null
        return if (isAllSelected) {
            sessionUis
        } else {
            sessionUis.filter { it.practiceType == state.selectedPracticeType }
        }
    }
}