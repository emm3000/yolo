package com.emm.yolo.feature.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.SelectAllWithAudioInfo
import com.emm.yolo.data.LogRepository
import com.emm.yolo.feature.home.calculateCurrentStreak
import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.PracticeType
import com.emm.yolo.feature.log.toDuration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

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

    private fun calculateStreak(sessionUis: List<EnglishSessionUi>): String {
        val sessionDates: List<Long> = sessionUis.map(EnglishSessionUi::sessionDateInMillis)
        return calculateCurrentStreak(sessionDates).toString()
    }

    private fun calculateAvg(sessionUis: List<EnglishSessionUi>): Duration? {
        return sessionUis
            .groupingBy { it.duration }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    private fun calculateWeekStreak(sessionUis: List<EnglishSessionUi>): Int {
        val zoneId = ZoneId.systemDefault()

        val startOfWeek = LocalDate.now(zoneId)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()

        val endOfWeek = LocalDate.now(zoneId)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            .atTime(23, 59, 59)
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

        return sessionUis.count { session -> session.sessionDateInMillis in startOfWeek..endOfWeek }
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