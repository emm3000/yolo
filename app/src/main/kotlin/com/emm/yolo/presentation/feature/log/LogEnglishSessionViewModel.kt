package com.emm.yolo.presentation.feature.log

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.data.Repository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

data class LogEnglishSessionUiState(
    val practiceType: PracticeType = PracticeType.entries.first(),
    val duration: Duration = Duration.entries.first(),
    val notes: String = "",
    val currentDateTime: LocalDateTime = LocalDateTime.now(),
)

sealed interface LogEnglishSessionAction {

    data class SetPracticeType(val practiceType: PracticeType) : LogEnglishSessionAction

    data class SetDuration(val duration: Duration) : LogEnglishSessionAction

    data class SetNotes(val notes: String) : LogEnglishSessionAction

    object Submit : LogEnglishSessionAction
}

class LogEnglishSessionViewModel(private val repository: Repository) : ViewModel() {

    var state by mutableStateOf(LogEnglishSessionUiState())
        private set

    fun onAction(action: LogEnglishSessionAction) = when(action) {
        LogEnglishSessionAction.Submit -> insertSession()
        else -> state = reducer(state, action)
    }

    private fun insertSession() = viewModelScope.launch {
        val insertSession = InsertSession(
            sessionDate = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            minutesPracticed = state.duration,
            practiceType = state.practiceType,
            notes = state.notes,
        )
        repository.insertSession(insertSession)
    }
}

fun reducer(state: LogEnglishSessionUiState, action: LogEnglishSessionAction): LogEnglishSessionUiState {
    val newState: LogEnglishSessionUiState = when (action) {
        is LogEnglishSessionAction.SetDuration -> {
            state.copy(duration = action.duration)
        }
        is LogEnglishSessionAction.SetNotes -> {
            state.copy(notes = action.notes)
        }
        is LogEnglishSessionAction.SetPracticeType -> {
            state.copy(practiceType = action.practiceType)
        }
        else -> state
    }
    return newState
}