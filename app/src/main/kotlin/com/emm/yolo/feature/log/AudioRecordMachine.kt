package com.emm.yolo.feature.log

import com.emm.yolo.data.AudioRecord

interface AudioRecordMachine {

    fun startRecording(fileName: String)

    fun stopRecording(): AudioRecord?

    fun deleteRecording(fileName: String)
}