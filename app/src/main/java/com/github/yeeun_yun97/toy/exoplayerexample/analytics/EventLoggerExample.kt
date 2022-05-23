package com.github.yeeun_yun97.toy.exoplayerexample.analytics

import com.google.android.exoplayer2.util.EventLogger

class EventLoggerExample private constructor() {
    companion object {
        fun newInstance(): EventLogger {
            // create trackSelector for additional logging or use null.
            val trackSelector = null
            return EventLogger(trackSelector)
        }
    }
}