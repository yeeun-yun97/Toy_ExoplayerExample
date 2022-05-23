package com.github.yeeun_yun97.toy.exoplayerexample.analytics

import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.PlaybackStats
import com.google.android.exoplayer2.analytics.PlaybackStatsListener

class PlaybackStatsListenerExample private constructor() {
    companion object {
        fun newInstance(): PlaybackStatsListener {

            //record processed and interpreted events
            val keepHistory = true



            val callback=
                PlaybackStatsListener.Callback {
                        eventTime, playbackStats ->
                    // playbackStats include 4 types of data

                    // summary metrics
                    playbackStats.totalPlayTimeMs
                    // adaptive playback quality metrics like..

                    // rendering quality metrics like ..
                    playbackStats.droppedFramesRate
                    // resource usage metrics like ..
                    playbackStats.totalBandwidthBytes


                    //contains data when keepHistory = true
                    playbackStats.playbackStateHistory
                    playbackStats.mediaTimeHistory
                    playbackStats.videoFormatHistory
                    playbackStats.fatalErrorHistory


                    // single playback analytics
                    playbackStats.totalPlayTimeMs
                    playbackStats.totalWaitTimeMs
                    playbackStats.totalRebufferCount

                    // aggregate anynytics data of multi playbacks
                    // combine multiple playbackStats by calling PlaybackStats.merge
                    val funMerge = PlaybackStats.merge()

                    //resulting playbackStatus object contain aggregated data of merged playbacks
                    val exampleListener = PlaybackStatsListener(false,null)
                    exampleListener.combinedPlaybackStats

                    //summary calculate metrics example
                    playbackStats.meanBandwidth
                    playbackStats.meanJoinTimeMs

                }
            return PlaybackStatsListener(keepHistory, callback)
        }
    }


}