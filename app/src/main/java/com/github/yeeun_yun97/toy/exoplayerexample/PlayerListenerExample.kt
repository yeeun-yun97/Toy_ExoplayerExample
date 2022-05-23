package com.github.yeeun_yun97.toy.exoplayerexample

import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.upstream.HttpDataSource

class PlayerListenerExample : Player.Listener {

    // playback state change
    override fun onPlaybackStateChanged(playbackState: Int) {
        val tag = "playback state changed"
        when (playbackState) {
            Player.STATE_IDLE -> Log.d(tag, "initialState")
            Player.STATE_BUFFERING -> Log.d(tag, "buffering")
            Player.STATE_READY -> Log.d(tag, "ready to play")
            Player.STATE_ENDED -> Log.d(tag, "finished playing")
        }
    }

    // playback error
    override fun onPlayerError(error: PlaybackException) {
        val cause = error.cause
        if (cause is HttpDataSource.HttpDataSourceException) {
            //request data
            val requestDataSpec = cause.dataSpec
            Log.d("error on request data ", requestDataSpec.toString())
            if (cause is HttpDataSource.InvalidResponseCodeException) {
                val responseData = cause.responseBody
                val responseCode = cause.responseCode
                Log.d("error code and response", "$responseCode :  $responseData")
            } else {
                val causeOf = cause.cause
                Log.d("error cause", causeOf?.toString() ?: "null")
            }
        }
    }

    // playlist transitions
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        val tag = "playback state changed"
        when (reason) {
            Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT ->
                Log.d(tag, "repeat same item")
            Player.MEDIA_ITEM_TRANSITION_REASON_AUTO ->
                Log.d(tag, "auto transition")
            Player.MEDIA_ITEM_TRANSITION_REASON_SEEK ->
                Log.d(tag, "seek like player.next()")
            Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED ->
                Log.d(tag, "playlist changed")
        }
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
        when (reason) {
            Player.DISCONTINUITY_REASON_SEEK ->
                // when seek called this way
                true
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)

        // you can see generic callback here.

        // pros
        // if you have same logic for multiple event
        // when you need access to player object
        // if you are interested in occurred together events

        // cons
        // but if interested in reasons for change,
        // use onPlayWhenReadyChanged, onMediaItemTransition, etc
    }

    // when media item added, removed moved
    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        when (reason) {
            Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED ->
                // update ui here to when modified playlist
                Log.d("playlist have changed", "-")
        }
    }

}