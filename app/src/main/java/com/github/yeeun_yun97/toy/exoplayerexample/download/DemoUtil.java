package com.github.yeeun_yun97.toy.exoplayerexample.download;

import android.content.Context;

import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.Executors;

public class DemoUtil {
    public static final String DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel";

    public static synchronized DownloadNotificationHelper getDownloadNotificationHelper(
            Context context) {
        return new DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID);
    }

    public static synchronized DownloadManager getDownloadManager(Context context) {
        DatabaseProvider databaseProvider = new StandaloneDatabaseProvider(context);
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);
        DataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
        File downloadDirectory = context.getExternalFilesDir(/* type= */ null);
            if (downloadDirectory == null) {
                downloadDirectory = context.getFilesDir();
            }
        File downloadContentDirectory =
                new File(downloadDirectory, "downloads");

        DownloadManager downloadManager =
                new DownloadManager(
                        context,
                        databaseProvider,
                        new SimpleCache(
                                downloadContentDirectory,
                                new NoOpCacheEvictor(),
                                databaseProvider),
                        httpDataSourceFactory,
                        Executors.newFixedThreadPool(/* nThreads= */ 6));
        return downloadManager;
    }

}
