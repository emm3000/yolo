package com.emm.yolo.feature.log

enum class Duration(
    val minutes: Long,
    val label: String,
) {

    FiveMinutes(minutes = 5, label = "5m"),
    TenMinutes(minutes = 10, label = "10m"),
    TwentyMinutes(minutes = 20, label = "20m"),
    ThirtyMinutesPlus(minutes = 30, label = "30m+"),
}

fun Long.toDuration(): Duration {
    return Duration.entries.find { it.minutes == this } ?: Duration.FiveMinutes
}

fun Duration.toNormalized(maxMinutes: Long = 30): Float {
    return (this.minutes.toFloat() / maxMinutes).coerceIn(0f, 1f)
}