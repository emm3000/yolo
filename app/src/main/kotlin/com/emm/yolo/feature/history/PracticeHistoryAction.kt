package com.emm.yolo.feature.history

import com.emm.yolo.feature.log.PracticeType

sealed interface PracticeHistoryAction {

    object AllPracticeTypes : PracticeHistoryAction

    data class PickPracticeType(val practiceType: PracticeType) : PracticeHistoryAction
}