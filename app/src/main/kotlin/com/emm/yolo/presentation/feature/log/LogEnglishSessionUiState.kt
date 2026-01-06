package com.emm.yolo.presentation.feature.log

import java.time.LocalDateTime

data class LogEnglishSessionUiState(
    val practiceType: PracticeType = PracticeType.entries.first(),
    val duration: Duration = Duration.entries.first(),
    val notes: String = "",
    val currentDateTime: LocalDateTime = LocalDateTime.now(),
    val statusMessage: String? = null,
)