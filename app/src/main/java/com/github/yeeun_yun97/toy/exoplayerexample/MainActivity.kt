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
import com.google.android.exoplayer2.source.ShuffleOrder
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var _player: ExoPlayer? = null
    private val player: ExoPlayer get() = _player!!

    private val exampleUrl1 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    private val exampleUrl2 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private val exampleUrl3 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    private val exampleUrl4 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
    private val exampleUrl5 =
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"

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
            Toast.makeText(
                this@MainActivity,
                "event 120 $messageType : ${payload.toString()}",
                Toast.LENGTH_LONG
            ).show()
        }.setLooper(Looper.getMainLooper())
            .setPosition(0, 120_000)
            .setDeleteAfterDelivery(false)
            .send()

        // set playlist and prepare
        setPlaylistAndPrepare(listOf(exampleUrl1, exampleUrl2))

        // modify playlist
        modifyPlaylist()

        //clear playlist
        player.clearMediaItems()

        // set entire playlist
        setPlaylistByList()

        /*
        // playlist count
        player.mediaItemCount

        // get media item by index
        player.getMediaItemAt(0)

        //current playing item index
        player.currentMediaItemIndex

        // check next, get next
        player.hasNextMediaItem()
        player.nextMediaItemIndex
        */


        // repeat mode
        setRepeatMode()

        // shuffle mode
        setShuffleMode()

    }

    private fun modifyPlaylist() {
        player.moveMediaItem(0, 1)
        player.removeMediaItem(0)
        player.addMediaItem(MediaItem.fromUri(exampleUrl3))
    }

    private fun setPlaylistByList() {
        val items: List<MediaItem> = listOf(
            buildMediaItem(exampleUrl2, "ex2"),
            MediaItem.fromUri(exampleUrl5),
            MediaItem.fromUri(exampleUrl3),
            MediaItem.fromUri(exampleUrl1),
            MediaItem.fromUri(exampleUrl4),
        )
        player.setMediaItems(items, true)
    }

    private fun buildMediaItem(url: String, mediaId: String = "", tag: String = ""): MediaItem {
        return MediaItem.Builder().setUri(url).setMediaId(mediaId).setTag(tag).build()
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
        for (url in urls) {
            val mediaItem = MediaItem.fromUri(url)
            player.addMediaItem(mediaItem)
        }
        player.prepare()
    }

    private fun playPlayList() {
        player.play()
    }

    private fun setRepeatMode() {
        player.repeatMode = Player.REPEAT_MODE_OFF //play next and don't repeat
        Player.REPEAT_MODE_ONE // play one and repeat
        Player.REPEAT_MODE_ALL // repeat playlist
    }

    private fun setShuffleMode() {
        // if turn on shuffleMode, seekToNextMediaItem will play randomized next item
        player.shuffleModeEnabled = true

        val array : IntArray = listOf(3, 1, 0, 4, 2).toIntArray()

        // set custom shuffle order
        player.setShuffleOrder(
            ShuffleOrder.DefaultShuffleOrder(array, 0L)
        )

    }


}