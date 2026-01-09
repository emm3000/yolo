package com.emm.yolo.feature.progress

import com.emm.yolo.feature.log.PracticeType

data class ProgressInsightsUiState(
    val consistency: String = "-",
    val bestStreak: String = "-",
    val totalTime: String = "-",
    val skillDistribution: Map<PracticeType, Float> = emptyMap(),
    val last14DaysActivity: List<Float> = emptyList(),
    val totalHours: String = "-",
)