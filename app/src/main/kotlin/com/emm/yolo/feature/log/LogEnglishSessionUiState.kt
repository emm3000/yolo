package com.emm.yolo.feature.log

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
) {

    val formattedDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH)
            return currentDateTime.format(formatter)
        }
}