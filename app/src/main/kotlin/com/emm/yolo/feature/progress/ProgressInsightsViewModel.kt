package com.emm.yolo.feature.progress

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.SelectAllWithAudioInfo
import com.emm.yolo.data.LogRepository
import com.emm.yolo.feature.history.EnglishSessionUi
import com.emm.yolo.feature.log.PracticeType
import com.emm.yolo.feature.log.toDuration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

class ProgressInsightsViewModel(private val logRepository: LogRepository) : ViewModel() {

    var state by mutableStateOf(ProgressInsightsUiState())
        private set

    init {
        fetchAll()
    }

    fun fetchAll() {
        logRepository.selectAllSessionsWithAudio()
            .map(::mapToUi)
            .onEach { sessionUis ->
                state = state.copy(
                    skillDistribution = sessionUis.practiceTypeProgress(),
                    last14DaysActivity = buildLast14DaysFromSessions(sessionUis),
                    totalHours = sessionUis
                        .totalMinutesLast14Days()
                        .toHoursAndMinutes(),
                )
            }
            .launchIn(viewModelScope)
    }

    private fun mapToUi(sessions: List<SelectAllWithAudioInfo>): List<EnglishSessionUi> = sessions.map { session ->
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
}

