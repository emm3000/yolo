package com.emm.yolo.presentation.feature.log

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.data.Repository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class LogEnglishSessionViewModel(private val repository: Repository) : ViewModel() {

    var state by mutableStateOf(LogEnglishSessionUiState())
        private set

    fun onAction(action: LogEnglishSessionAction) = when(action) {
        LogEnglishSessionAction.Submit -> insertSession()
        else -> state = reducer(state, action)
    }

    private fun insertSession() = viewModelScope.launch {
        try {
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
            state = state.copy(statusMessage = "Log session successfully")
        } catch (_: SQLiteConstraintException) {
            state = state.copy(statusMessage = "Actually this session already exists")
        }
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