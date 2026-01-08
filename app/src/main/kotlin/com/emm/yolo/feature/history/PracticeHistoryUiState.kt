package com.emm.yolo.feature.history

import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.PracticeType

data class PracticeHistoryUiState(
    val thisWeek: String = "-",
    val streak: String = "-",
    val avgTime: Duration? = null,
    val sessions: List<EnglishSessionUi> = emptyList(),
    val filteredSessions: List<EnglishSessionUi> = emptyList(),
    val selectedPracticeType: PracticeType? = null,
)