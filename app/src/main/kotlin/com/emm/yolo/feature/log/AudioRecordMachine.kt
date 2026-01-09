package com.emm.yolo.feature.log

interface AudioRecordMachine {

    fun startRecording(fileName: String)

    fun stopRecording(): AudioRecord?

    fun stopAndDelete()

    fun deleteRecording(fileName: String)
}