package com.github.yeeun_yun97.toy.exoplayerexample

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ads.AdsLoader
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSource.Factory


class MediaSourceFactoryExample private constructor() {

    companion object{
        /*
        fun newInstance(playerView:StyledPlayerView) : MediaSource.Factory{

//            val dataSourceFactory = object: Factory{
//                // when use player.createMediaItem, this is called internally
//                override fun createDataSource(): DataSource {
//                   var dataSource : DataSource? = null
//                }
//            }

//            val adsLoaderProvider = object: AdsLoader.Provider{
//                // when use player.createMediaItem, this is called internally
//                override fun getAdsLoader(adsConfiguration: MediaItem.AdsConfiguration): AdsLoader? {
//                    var adsLoader : AdsLoader? = null
//                }
//            }

            return DefaultMediaSourceFactory(dataSourceFactory) //set cache Factory
                    .setAdsLoaderProvider(adsLoaderProvider) //set Ad Loader
                    .setAdViewProvider(playerView) // set playerView
        }

         */
    }


}