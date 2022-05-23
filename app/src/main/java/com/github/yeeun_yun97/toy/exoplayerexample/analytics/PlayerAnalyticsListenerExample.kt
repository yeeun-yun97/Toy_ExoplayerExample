package com.github.yeeun_yun97.toy.exoplayerexample.analytics

import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime


class PlayerAnalyticsListenerExample : AnalyticsListener{

    override fun onPlaybackStateChanged(eventTime: EventTime, state: Int) {
        // wall clock time of event
        eventTime.realtimeMs



        // define playlist and item ( all )
        eventTime.timeline
        eventTime.windowIndex
        eventTime.mediaPeriodId // contains optional information if event belongs to ad

        // playback position in the item ( all )
        eventTime.eventPlaybackPositionMs



        // define playlist and item ( only current playing item )
        eventTime.currentTimeline
        eventTime.currentWindowIndex
        eventTime.currentMediaPeriodId // contains optional information if event belongs to ad

        // playback position in the item ( only current playing item )
        eventTime.currentPlaybackPositionMs
    }

}