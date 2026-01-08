package com.emm.yolo.feature.log.recorder

data class AudioRecord(
    val path: String,
    val name: String,
    val durationMs: Long,
)