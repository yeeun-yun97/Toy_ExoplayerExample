package com.github.yeeun_yun97.toy.exoplayerexample

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.IntRange
import com.github.yeeun_yun97.toy.exoplayerexample.analytics.EventLoggerExample
import com.github.yeeun_yun97.toy.exoplayerexample.analytics.PlaybackStatsListenerExample
import com.github.yeeun_yun97.toy.exoplayerexample.analytics.PlayerAnalyticsListenerExample
import com.github.yeeun_yun97.toy.exoplayerexample.databinding.ActivityMainBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.C.SELECTION_FLAG_DEFAULT
import com.google.android.exoplayer2.MediaItem.AdsConfiguration
import com.google.android.exoplayer2.MediaItem.SubtitleConfiguration
import com.google.android.exoplayer2.source.ShuffleOrder
import com.google.android.exoplayer2.source.TrackGroup
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionOverrides
import com.google.android.exoplayer2.ui.StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING
import com.google.android.exoplayer2.util.MimeTypes
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
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


       // customize track selector
        val trackSelector = DefaultTrackSelector(this)
        trackSelector.parameters = trackSelector.buildUponParameters()
            .setAllowVideoMixedMimeTypeAdaptiveness(true)           // set advanced customization options here
            .build()
        _player = ExoPlayer.Builder(this).setTrackSelector(trackSelector).build()


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

        // track info 1. by listener handle trackinfo change event
        player.addListener(object : Player.Listener {
            override fun onTracksInfoChanged(tracksInfo: TracksInfo) {
                handleCurrentTrackInfo(tracksInfo)
            }
        })

        // track info 2. get current trackInfo
        val tracksInfo = player.currentTracksInfo
        this.handleCurrentTrackInfo(tracksInfo)


        // set constraint to tracks (for simplify selecting track..ect)

        // set max size and set preferred audio language
        player.trackSelectionParameters =
            player.trackSelectionParameters.buildUpon()
                .setMaxVideoSizeSd()                            // set max size to 1279*719
                .setPreferredAudioLanguage("ko-KR")             // set preferred audio language using IETF BCP 47 conformant tag
                .build()

        // select audio track group and prevent other audio track groups
        /*val audioTrackGroup : TrackGroup? = null
        val overrides = TrackSelectionOverrides.Builder()
            .setOverrideForType(TrackSelectionOverrides.TrackSelectionOverride(audioTrackGroup!!))
            .build()
        player.trackSelectionParameters = player.trackSelectionParameters.buildUpon()
            .setTrackSelectionOverrides(overrides).build()
            */

        // disable track types or groups
//        player.trackSelectionParameters=
//                player.trackSelectionParameters.buildUpon()
//                    .setDisabledTrackTypes(ImmutableSet.of(C.TRACK_TYPE_VIDEO)) // disable track type video
//                    .build()

        binding.playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)




        // repeat mode
        setRepeatMode()

        // shuffle mode
        setShuffleMode()

    }

    private fun handleCurrentTrackInfo(tracksInfo: TracksInfo) {
        for (groupInfo in tracksInfo.trackGroupInfos) {   // 그룹에 대한 정보.
            val trackType =
                groupInfo.trackType         // trackType : None, unknown, default, audio, video, text, image, metadata, cameraMotion..
            groupInfo.isSelected                        // 하나라도 그룹 내의 트랙이 재생을 위해 선택되어 있으면 true.
            groupInfo.isSupported                       // 하나라도 그룹 내의 트랙이 기기의 성능 안에 가능하면 true
            val group = groupInfo.trackGroup            // groupInfo가 설명하던 그룹을 반환한다.
            for (i in 0 until group.length) {
                groupInfo.isTrackSupported(i)           // 그룹의 i번째 트랙이 기기의 성능 안에 가능한가.
                groupInfo.isTrackSelected(i)            // 그룹의 i번째 트략이 선택되어 있는가.
                group.getFormat(i)                      // (width, height, frameRate, codecs, metadata 등 )트랙에 대한 형식 정보를 가져온다.
            }
        }
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

    private fun sideloadSubtitleMediaItem(videoUri: String, subtitleUri: String): MediaItem {
        val subtitle = SubtitleConfiguration.Builder(Uri.parse(subtitleUri))
            .setMimeType(MimeTypes.APPLICATION_SUBRIP) // The correct MIME type (required).
            .setLanguage("ko-kr") // The subtitle language (optional).
            .setSelectionFlags(SELECTION_FLAG_DEFAULT) // Selection flags for the track (optional).
            .build()
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(videoUri)
            .setSubtitleConfigurations(ImmutableList.of(subtitle))
            .build()
        return mediaItem
    }

    private fun clip(
        videoUri: String,
        @IntRange(from = 0) startPositionMs: Long,
        endPositionMs: Long
    ): MediaItem {
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(videoUri)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(startPositionMs)
                    .setEndPositionMs(endPositionMs)
                    .build()
            )
            .build()
        return mediaItem
    }

    private fun adMediaItem(videoUri: String, adTagUri: Uri): MediaItem {
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(videoUri)
            .setAdsConfiguration(
                AdsConfiguration.Builder(adTagUri).build()
            )
            .build()
        return mediaItem
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

        val array: IntArray = listOf(3, 1, 0, 4, 2).toIntArray()

        // set custom shuffle order
        player.setShuffleOrder(
            ShuffleOrder.DefaultShuffleOrder(array, 0L)
        )

    }


}