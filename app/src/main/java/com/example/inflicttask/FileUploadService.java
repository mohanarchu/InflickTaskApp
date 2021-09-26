package com.example.inflicttask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileUploadService extends Service {

    public static final String START_UPLOAD = "START_UPLOAD";
    private static final int NOTIFICATION_ID = 111;
    static Bitmap bitmap;

    public static void startUpload(Context context, Bitmap bitmaps) {
        Intent intent = new Intent(context, FileUploadService.class);
        intent.setAction(START_UPLOAD);
        FileUploadService.bitmap =  bitmaps;
        context.startService(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            if (START_UPLOAD.equals(action)) {
                showUploadNotification();
                new UploadTask(DropboxClient.getClient(DropboxClient.retrieveAccessToken(getApplicationContext())),
                        FileUploadService.bitmap, getApplicationContext(), new UploadInterface() {
                    @Override
                    public void uploadCompleted() {
                        hideNotification();
                        Toast.makeText(getApplicationContext(),"File upload completed",Toast.LENGTH_SHORT).show();
                    }
                }).execute();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }



    private void hideNotification() {
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        stopForeground(true);
    }

    private void showUploadNotification(){
        String channelName = "File upload service";
        NotificationChannel chan = null;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = new NotificationChannel(String.valueOf(NOTIFICATION_ID), channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, String.valueOf(NOTIFICATION_ID));
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("File is uploading....")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }
}