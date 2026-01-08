package com.emm.yolo.feature.log

import com.emm.yolo.feature.log.recorder.AudioRecord

sealed interface LogEnglishSessionAction {

    data class SetPracticeType(val practiceType: PracticeType) : LogEnglishSessionAction

    data class SetDuration(val duration: Duration) : LogEnglishSessionAction

    data class SetNotes(val notes: String) : LogEnglishSessionAction

    object StartRecording : LogEnglishSessionAction

    object StopRecording : LogEnglishSessionAction

    data class PlayAudio(val record: AudioRecord) : LogEnglishSessionAction

    data class PauseAudio(val record: AudioRecord) : LogEnglishSessionAction

    data class DeleteAudio(val record: AudioRecord) : LogEnglishSessionAction

    object Submit : LogEnglishSessionAction
}