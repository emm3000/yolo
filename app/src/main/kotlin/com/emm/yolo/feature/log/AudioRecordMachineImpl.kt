@file:Suppress("DEPRECATION")

package com.emm.yolo.feature.log

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.emm.yolo.data.AudioRecord
import java.io.File

class AudioRecordMachineImpl(private val context: Context) : AudioRecordMachine {

    private var mediaRecorder: MediaRecorder? = null
    private var currentFile: File? = null

    override fun startRecording(fileName: String) {
        val file = File(context.filesDir, "$fileName.m4a")
        currentFile = file

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
    }

    override fun stopRecording(): AudioRecord? {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        return currentFile?.let {
            AudioRecord(
                path = it.absolutePath,
                name = it.name,
                durationMs = it.length()
            )
        }
    }

    override fun deleteRecording(fileName: String) {
        val file = File(fileName)
        if (file.exists()) file.delete()
    }
}