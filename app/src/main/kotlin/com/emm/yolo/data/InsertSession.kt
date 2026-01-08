package com.emm.yolo.data

import com.emm.yolo.feature.log.Duration
import com.emm.yolo.feature.log.PracticeType

class InsertSession(
    val sessionDate: Long,
    val sessionHour: Long,
    val minutesPracticed: Duration,
    val practiceType: PracticeType,
    val confidenceLevel: Long = 5,
    val discomfortLevel: Long = 5,
    val notes: String,
)