package com.github.yeeun_yun97.toy.exoplayerexample.download

import android.app.Notification
import android.content.Context
import com.github.yeeun_yun97.toy.exoplayerexample.R
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.util.NotificationUtil
import com.google.android.exoplayer2.util.Util
import java.lang.Exception

class NotificationHelperExample(
    val context: Context,
    val notificationHelper: DownloadNotificationHelper,
    firstNotificationId: Int
) : DownloadManager.Listener {
    private var nextNotificationId: Int = firstNotificationId

    override fun onDownloadChanged(
        downloadManager: DownloadManager,
        download: Download,
        finalException: Exception?
    ) {

        val notification: Notification
        if (download.state == Download.STATE_COMPLETED) {
            notification =
                notificationHelper.buildDownloadCompletedNotification(
                    context,
                    R.drawable.ic_download_done,
                    /* contentIntent= */ null,
                    Util.fromUtf8Bytes(download.request.data)
                );
        } else if (download.state == Download.STATE_FAILED) {
            notification =
                notificationHelper.buildDownloadFailedNotification(
                    context,
                    R.drawable.ic_download_done,
                    /* contentIntent= */ null,
                    Util.fromUtf8Bytes(download.request.data)
                );
        } else {
            return;
        }
        NotificationUtil.setNotification(context, nextNotificationId++, notification);
    }


}