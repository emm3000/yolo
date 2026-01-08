package com.emm.yolo.feature.log

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.emm.yolo.data.Repository
import com.emm.yolo.feature.log.recorder.AudioRecord
import com.emm.yolo.feature.log.recorder.AudioRecordMachine
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.random.Random

class LogEnglishSessionViewModel(
    private val repository: Repository,
    private val audioRecordMachine: AudioRecordMachine,
    application: Application,
) : ViewModel() {

    var state by mutableStateOf(LogEnglishSessionUiState())
        private set

    private val player: ExoPlayer = ExoPlayer.Builder(application)
        .build()

    private val dispatcher = PlayerDispatcher(player) {
        state = state.copy(isPlaying = false, currentRecord = null)
    }

    init {
        player.addListener(dispatcher)
    }

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
        player.clearMediaItems()
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

    private fun preparePlayer(path: String) {
        val uri = Uri.fromFile(File(path))
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
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

        preparePlayer(record.path)
        player.play()
        state = state.copy(isPlaying = true, currentRecord = record)
    }

    fun discard(record: AudioRecord) {
        if (state.currentRecord == record && state.isPlaying) {
            player.stop()
            player.clearMediaItems()
        }

        audioRecordMachine.deleteRecording(record.path)
        state = state.copy(
            records = state.records.filterNot { audioRecord -> audioRecord == record },
            playerState = PlayerState.Idle,
            currentRecord = if (state.records == record) null else state.currentRecord,
            isPlaying = if (state.records == record) false else state.isPlaying,
        )
    }

    fun save() {
        state = state.copy(playerState = PlayerState.Idle)
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
            val sessionId: Long = repository.insertSession(insertSession)
            state.records.forEach { audioRecord ->
                repository.insertAudio(
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
        player.removeListener(dispatcher)
        player.release()
        super.onCleared()
    }
}

fun testDate(): List<InsertSession> {
    val date: LocalDate = LocalDate.now()

    return (1L..1000L).map {
        InsertSession(
            sessionDate = date.minusDays(it)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            sessionHour = LocalTime.now().toSecondOfDay().toLong(),
            minutesPracticed = Duration.FiveMinutes,
            practiceType = PracticeType.Speaking,
            confidenceLevel = 7085,
            discomfortLevel = 4987,
            notes = "atqui $it"
        )
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