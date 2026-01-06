package com.emm.yolo.presentation.feature.log

class InsertSession(
    val sessionDate: Long,
    val sessionHour: Long,
    val minutesPracticed: Duration,
    val practiceType: PracticeType,
    val confidenceLevel: Long = 5,
    val discomfortLevel: Long = 5,
    val notes: String,
)