package com.emm.yolo.feature.progress

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
}

