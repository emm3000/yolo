package com.emm.yolo.feature.log.recorder

interface AudioRecordMachine {

    fun startRecording(fileName: String)

    fun stopRecording(): AudioRecord?

    fun deleteRecording(fileName: String)
}