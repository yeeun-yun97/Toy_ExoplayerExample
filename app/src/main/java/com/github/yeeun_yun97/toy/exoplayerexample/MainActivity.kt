package com.github.yeeun_yun97.toy.exoplayerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import com.github.yeeun_yun97.toy.exoplayerexample.analytics.EventLoggerExample
import com.github.yeeun_yun97.toy.exoplayerexample.analytics.PlaybackStatsListenerExample
import com.github.yeeun_yun97.toy.exoplayerexample.analytics.PlayerAnalyticsListenerExample
import com.github.yeeun_yun97.toy.exoplayerexample.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.PlayerMessage
import com.google.android.exoplayer2.trackselection.MappingTrackSelector

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        // create player and bind with view
        _player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        // add player listener
        player.addListener(PlayerListenerExample())

        //analytics listener
        player.addAnalyticsListener(PlayerAnalyticsListenerExample())
        player.addAnalyticsListener(PlaybackStatsListenerExample.newInstance())
        player.addAnalyticsListener(EventLoggerExample.newInstance())

        // fire event at specified playback position
        player.createMessage { messageType, payload ->
            // Do something at the specified playback position.
            Toast.makeText(this@MainActivity, "event 120 $messageType : ${payload.toString()}", Toast.LENGTH_LONG).show()
        }.setLooper(Looper.getMainLooper())
            .setPosition(0, 120_000)
            .setDeleteAfterDelivery(false)
            .send()

        // set playlist and prepare
        val exampleVideoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        setPlaylistAndPrepare(listOf(exampleVideoUrl))
    }

    override fun onResume() {
        super.onResume()
        playPlayList()
    }

    override fun onStop() {
        super.onStop()

        // release player
        player.release()
        _player = null
    }

    private fun setPlaylistAndPrepare(urls: List<String>) {
        for(url in urls) {
            val mediaItem = MediaItem.fromUri(url)
            player.setMediaItem(mediaItem)
        }
        player.prepare()
    }

    private fun playPlayList() {
        player.play()
    }


}