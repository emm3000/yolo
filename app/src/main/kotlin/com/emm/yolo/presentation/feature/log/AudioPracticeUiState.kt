package com.emm.yolo.presentation.feature.log

import java.io.File

sealed interface AudioPracticeUiState {

    object Idle : AudioPracticeUiState

    object Recording : AudioPracticeUiState

    data class Preview(
        val file: File,
        val durationSeconds: Int
    ) : AudioPracticeUiState
}