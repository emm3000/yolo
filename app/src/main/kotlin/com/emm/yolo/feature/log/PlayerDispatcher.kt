package com.emm.yolo.feature.log

import androidx.media3.common.Player

class PlayerDispatcher(
    private val player: Player,
    private val onStateUpdated: () -> Unit,
) : Player.Listener {

    override fun onPlaybackStateChanged(state: Int) {
        if (state == Player.STATE_ENDED) {
            player.pause()
            player.clearMediaItems()
            onStateUpdated()
        }
    }
}