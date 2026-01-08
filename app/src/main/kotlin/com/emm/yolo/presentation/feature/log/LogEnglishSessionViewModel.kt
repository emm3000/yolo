package com.emm.yolo.presentation.feature.log

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.emm.yolo.data.Repository
import com.emm.yolo.presentation.feature.log.recorder.AudioRecord
import com.emm.yolo.presentation.feature.log.recorder.AudioRecordMachine
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

    private val player: ExoPlayer = ExoPlayer.Builder(application).build()

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    player.seekTo(0)
                    player.pause()
                    this@LogEnglishSessionViewModel.state = this@LogEnglishSessionViewModel.state.copy(isPlaying = false)
                }
            }
        })
    }

    fun onAction(action: LogEnglishSessionAction) = when (action) {
        LogEnglishSessionAction.Submit -> insertSession()
        is LogEnglishSessionAction.StartRecording -> startRecording()
        is LogEnglishSessionAction.StopRecording -> stopRecording()
        is LogEnglishSessionAction.PlayAudio -> playReview()
        is LogEnglishSessionAction.PauseAudio -> toggleAudioReview()
        is LogEnglishSessionAction.DeleteAudio -> discard()
        else -> state = reducer(state, action)
    }

    fun startRecording() {
        audioRecordMachine.startRecording("temp_audio_${System.currentTimeMillis()}")
        state = state.copy(playerState = PlayerState.Recording)
    }

    fun stopRecording() {
        val record: AudioRecord? = audioRecordMachine.stopRecording()
        state = state.copy(
            lastRecord = record,
            playerState = PlayerState.Reviewing,
        )
        record?.let { preparePlayer(it.path) }
    }

    private fun preparePlayer(path: String) {
        val uri = Uri.fromFile(File(path))
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    private fun toggleAudioReview() {
        if (player.isPlaying) {
            player.pause()
            state = state.copy(isPlaying = false)
        } else {
            player.play()
            state = state.copy(isPlaying = true)
        }
    }

    fun playReview() {
        val lastRecord: AudioRecord? = state.lastRecord
        lastRecord?.let {
            player.play()
            state = state.copy(isPlaying = true)
        }
    }

    fun discard() {
        player.stop()
        player.clearMediaItems()
        val lastRecord = state.lastRecord
        lastRecord?.let {
            audioRecordMachine.deleteRecording(it.path)
        }
        state = state.copy(
            lastRecord = null,
            playerState = PlayerState.Idle,
            isPlaying = false,
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
            val record: AudioRecord = state.lastRecord ?: return@launch run {
                state = state.copy(statusMessage = "Log session successfully")
            }
            repository.insertAudio(
                sessionId = sessionId,
                filePath = record.path,
                durationSeconds = Random.nextLong(),
                prompt = state.notes,
            )
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