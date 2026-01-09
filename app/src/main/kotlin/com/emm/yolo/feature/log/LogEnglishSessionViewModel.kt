package com.emm.yolo.feature.log

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emm.yolo.data.LogRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.random.Random

class LogEnglishSessionViewModel(
    private val logRepository: LogRepository,
    private val audioRecordMachine: AudioRecordMachine,
    playerFactory: AudioPlayer.Factory,
    application: Application,
) : ViewModel() {

    var state by mutableStateOf(LogEnglishSessionUiState())
        private set

    private val player: AudioPlayer = playerFactory.create(
        context = application,
        onStopped = { state = state.copy(isPlaying = false, currentRecord = null) }
    )

    fun onAction(action: LogEnglishSessionAction) = when (action) {
        LogEnglishSessionAction.Submit -> insertSession()
        is LogEnglishSessionAction.StartRecording -> startRecording()
        is LogEnglishSessionAction.StopRecording -> stopRecording()
        is LogEnglishSessionAction.PlayAudio -> toggleAudioReview(action.record)
        is LogEnglishSessionAction.PauseAudio -> toggleAudioReview(action.record)
        is LogEnglishSessionAction.DeleteAudio -> discard(action.record)
        else -> state = reducer(state, action)
    }

    fun startRecording() {
        player.stop()
        audioRecordMachine.startRecording("temp_audio_${System.currentTimeMillis()}")
        state = state.copy(playerState = PlayerState.Recording, isPlaying = false, currentRecord = null)
    }

    fun stopRecording() {
        val record: AudioRecord = audioRecordMachine.stopRecording() ?: return
        state = state.copy(
            records = state.records + record,
            playerState = PlayerState.Idle,
            isPlaying = false
        )
    }

    fun stopAndDeleteRecording() {
        audioRecordMachine.stopAndDelete()
    }

    private fun toggleAudioReview(record: AudioRecord) {
        if (player.isPlaying) {
            player.pause()
            state = state.copy(isPlaying = false)
        } else {
            playReview(record)
        }
    }

    fun playReview(record: AudioRecord) {
        if (state.currentRecord == record) {
            player.play()
            state = state.copy(isPlaying = true)
            return
        }

        player.prepareAndPlay(record.path)
        state = state.copy(isPlaying = true, currentRecord = record)
    }

    fun discard(record: AudioRecord) {
        if (state.currentRecord == record && state.isPlaying) {
            player.stop()
        }

        audioRecordMachine.deleteRecording(record.path)
        state = state.copy(
            records = state.records.filterNot { audioRecord -> audioRecord == record },
            playerState = PlayerState.Idle,
            currentRecord = if (state.records == record) null else state.currentRecord,
            isPlaying = if (state.records == record) false else state.isPlaying,
        )
    }

    private fun insertSession() = viewModelScope.launch {
        try {
            val insertSession = InsertSession(
                sessionDate = LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                sessionHour = LocalTime.now().toSecondOfDay().toLong(),
                minutesPracticed = state.duration,
                practiceType = state.practiceType,
                notes = state.notes,
            )
            val sessionId: Long = logRepository.insertSession(insertSession)
            state.records.forEach { audioRecord ->
                logRepository.insertAudio(
                    sessionId = sessionId,
                    filePath = audioRecord.path,
                    durationSeconds = Random.nextLong(),
                    prompt = state.notes,
                )
            }
            state = state.copy(statusMessage = "Log session successfully")
        } catch (_: SQLiteConstraintException) {
            state = state.copy(statusMessage = "Actually this session already exists")
        }
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}

fun reducer(state: LogEnglishSessionUiState, action: LogEnglishSessionAction): LogEnglishSessionUiState {
    val newState: LogEnglishSessionUiState = when (action) {
        is LogEnglishSessionAction.SetDuration -> {
            state.copy(duration = action.duration)
        }

        is LogEnglishSessionAction.SetNotes -> {
            state.copy(notes = action.notes)
        }

        is LogEnglishSessionAction.SetPracticeType -> {
            state.copy(practiceType = action.practiceType)
        }

        else -> state
    }
    return newState
}