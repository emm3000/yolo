package com.emm.yolo.presentation.feature.log

sealed interface LogEnglishSessionAction {

    data class SetPracticeType(val practiceType: PracticeType) : LogEnglishSessionAction

    data class SetDuration(val duration: Duration) : LogEnglishSessionAction

    data class SetNotes(val notes: String) : LogEnglishSessionAction

    object Submit : LogEnglishSessionAction
}