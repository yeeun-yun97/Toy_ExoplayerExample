package com.github.yeeun_yun97.toy.exoplayerexample.download


import android.app.Notification
import com.github.yeeun_yun97.toy.exoplayerexample.R
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.util.Util


class DownloadServiceExample(
    val JOB_ID: Int = 1,
    val FOREGROUND_NOTIFICATION_ID: Int = 1,
    val DOWNLOAD_NOTIFICATION_CHANNEL_ID: String = "download_Channel"
) : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.noti_channel_name,
    0
) {
    override fun getDownloadManager(): DownloadManager {
        val downloadManager: DownloadManager = DemoUtil.getDownloadManager( /* context= */this)
        val downloadNotificationHelper: DownloadNotificationHelper =
            DemoUtil.getDownloadNotificationHelper( /* context= */this)
        downloadManager.addListener(
            NotificationHelperExample(
                this, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1
            )
        )
        return downloadManager
    }

    override fun getScheduler(): Scheduler? {
        return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return DemoUtil.getDownloadNotificationHelper(/* context= */ this)
            .buildProgressNotification(
                /* context= */ this,
                R.drawable.ic_download,
                /* contentIntent= */ null,
                /* message= */ null,
                downloads,
                notMetRequirements
            );
    }


}