package com.emm.yolo.presentation.feature.log

import com.emm.yolo.presentation.feature.log.recorder.AudioRecord
import java.time.LocalDateTime

data class LogEnglishSessionUiState(
    val practiceType: PracticeType = PracticeType.entries.first(),
    val duration: Duration = Duration.entries.first(),
    val notes: String = "",
    val currentDateTime: LocalDateTime = LocalDateTime.now(),
    val statusMessage: String? = null,
    val playerState: PlayerState = PlayerState.Idle,
    val currentRecord: AudioRecord? = null,
    val records: List<AudioRecord> = emptyList(),
    val isPlaying: Boolean = false,
)