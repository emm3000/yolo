package com.emm.yolo.presentation.feature.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.EnglishSession
import com.emm.yolo.data.Repository
import com.emm.yolo.presentation.feature.log.Duration
import com.emm.yolo.presentation.feature.log.PracticeType
import com.emm.yolo.presentation.feature.log.toDuration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
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

data class PracticeHistoryUiState(
    val thisWeek: String = "-",
    val streak: String = "-",
    val avgTime: Duration? = null,
    val sessions: List<EnglishSessionUi> = emptyList(),
    val filteredSessions: List<EnglishSessionUi> = emptyList(),
    val selectedPracticeType: PracticeType? = null,
)

sealed interface PracticeHistoryAction {

    object AllPracticeTypes : PracticeHistoryAction

    data class PickPracticeType(val practiceType: PracticeType) : PracticeHistoryAction
}

class PracticeHistoryViewModel(private val repository: Repository) : ViewModel() {

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
        repository.selectAllSessions()
            .map(::mapToUi)
            .onEach { sessionUis ->
                val filteredSession: List<EnglishSessionUi> = filteringSessions(sessionUis)
                state = state.copy(sessions = sessionUis, filteredSessions = filteredSession)
            }
            .launchIn(viewModelScope)
    }

    private fun mapToUi(sessions: List<EnglishSession>): List<EnglishSessionUi> = sessions.map { session ->
        EnglishSessionUi(
            id = session.id,
            sessionDate = Instant
                .ofEpochMilli(session.sessionDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),
            sessionHour = LocalTime.ofSecondOfDay(session.sessionHour),
            minutesPracticed = session.minutesPracticed.toDuration(),
            practiceType = PracticeType.valueOf(session.practiceType),
            confidenceLevel = session.confidenceLevel,
            discomfortLevel = session.discomfortLevel,
            notes = session.notes,
            createdAt = session.createdAt,
            updatedAt = session.updatedAt,
        )
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