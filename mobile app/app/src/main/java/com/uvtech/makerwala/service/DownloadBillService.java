package com.uvtech.makerwala.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;
import com.uvtech.makerwala.ApplicationLoader;
import com.uvtech.makerwala.R;
import com.uvtech.makerwala.helpers.FileHelper;

import java.io.File;

public class DownloadBillService extends Service {

    public final static int NOTIFICATION_ID = 100000;

    NotificationManager notificationManager;
    NotificationCompat.Builder nbuilder;
    ThinDownloadManager downloadManager;
    private RetryPolicy retryPolicy;

    private String downloadUrl = "";
    private String destUrl = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = new ThinDownloadManager(1);
        retryPolicy = new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public float getBackOffMultiplier() {
                return 0;
            }

            @Override
            public void retry() {

            }
        };
        createNotification();
    }

    @Override
    public void onDestroy() {
        downloadManager.release();
        cancelNotification();
        super.onDestroy();
    }

    private void createNotification() {
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            channelId = "";
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nbuilder = new NotificationCompat.Builder(this, channelId);
        nbuilder.setSmallIcon(R.mipmap.ic_launcher);
        nbuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        nbuilder.setContentTitle(getResources().getString(R.string.str_downloading));
        nbuilder.setContentText(getResources().getString(R.string.str_loading));
        nbuilder.setOngoing(true);
        nbuilder.build();
        notificationManager.notify(NOTIFICATION_ID, nbuilder.build());
        startForeground(NOTIFICATION_ID, nbuilder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "my_service";
        String channelName = "My Background Service";
        NotificationChannel notificationChannel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        return channelId;
    }

    private void updateProgressNotification(int progress) {
        try {
            nbuilder.setProgress(100, progress, false);
            nbuilder.setContentTitle(getResources().getString(R.string.str_downloading));
            nbuilder.setContentText(getResources().getString(R.string.str_downloading));
            notificationManager.notify(NOTIFICATION_ID, nbuilder.build());
        } catch (Exception e) {
            Log.e("updateProgressNotiex", e.getMessage());
        }
    }

    private void setCompletedNotification() {
        nbuilder.setContentTitle(getResources().getString(R.string.str_invoice_downloaded_successfully));
        nbuilder.setProgress(0, 0, false);
        nbuilder.setContentText(getResources().getString(R.string.str_invoice_downloaded_successfully)
                + " in " + new File(destUrl).getAbsolutePath());
        nbuilder.setOngoing(false);
        nbuilder.setAutoCancel(true);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(FileProvider.getUriForFile(this,
                getResources().getString(R.string.file_provider_authority), new File(destUrl)), "video/*");
//        startActivity(intent);
        PendingIntent dismissIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        nbuilder.setContentIntent(dismissIntent);
        notificationManager.notify(NOTIFICATION_ID, nbuilder.build());
    }

    private void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
        stopForeground(true);
    }

    private void startDownload() {
        Uri downloadUri = Uri.parse(downloadUrl);
        Uri destinationUri = Uri.fromFile(new File(destUrl));//FileProvider.getUriForFile(this,
        //getResources().getString(R.string.file_provider_authority), new File(destUrl));
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setRetryPolicy(retryPolicy)
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadContext(this)
                .setStatusListener(myDownloadStatusListener);
        downloadManager.add(downloadRequest);
    }

    DownloadStatusListenerV1 myDownloadStatusListener = new DownloadStatusListenerV1() {
        @Override
        public void onDownloadComplete(DownloadRequest downloadRequest) {
            cancelNotification();
            setCompletedNotification();
        }

        @Override
        public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode,
                                     String errorMessage) {
            cancelNotification();
            stopSelf();
        }

        @Override
        public void onProgress(DownloadRequest downloadRequest, long totalBytes,
                               long downloadedBytes, int progress) {
            updateProgressNotification(progress);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ApplicationLoader.getInstance().getVideoUrl().equalsIgnoreCase("")) {
            downloadUrl = ApplicationLoader.getInstance().getVideoUrl();

//            destUrl = FileHelper.getGenerateDateTimeFileName(this,ApplicationLoader.getInstance().getVideoExt());
//            destUrl = FileHelper.getGenerateDateTimeFileName(ApplicationLoader.getInstance().getVideoExt());
            startDownload();
        } else {
            stopSelf();
        }
        return START_STICKY;
    }
}
