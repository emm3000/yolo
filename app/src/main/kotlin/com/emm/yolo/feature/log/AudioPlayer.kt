package com.emm.yolo.feature.log

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.io.File

class AudioPlayer(context: Context, onStopped: () -> Unit) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    private val dispatcher = PlayerDispatcher(player, onStopped)

    val isPlaying: Boolean get() = player.isPlaying

    init {
        player.addListener(dispatcher)
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun prepareAndPlay(path: String) {
        val uri = Uri.fromFile(File(path))
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun release() {
        player.removeListener(dispatcher)
        player.release()
    }

    class Factory {

        fun create(
            context: Context,
            onStopped: () -> Unit,
        ): AudioPlayer = AudioPlayer(context, onStopped)
    }
}