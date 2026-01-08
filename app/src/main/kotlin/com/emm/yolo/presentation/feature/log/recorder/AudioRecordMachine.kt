package com.emm.yolo.presentation.feature.log.recorder

interface AudioRecordMachine {

    fun startRecording(fileName: String)

    fun stopRecording(): AudioRecord?

    fun deleteRecording(fileName: String)
}