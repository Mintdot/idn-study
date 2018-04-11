package com.mintdot.idnstudy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String message;
        String target;

        // 데이터 메시지
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            message = remoteMessage.getData().get("m");
            target = remoteMessage.getData().get("target");
            Log.d(TAG, "Message: " + message);
            sendNotification(message, target);
        }

        // 알림 메시지
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//            message = remoteMessage.getNotification().getBody();
//            target = null;
//            Log.d(TAG, "Message: " + message);
//            sendNotification(message, target);
//        }
    }

    private void sendNotification(String message, String target) {
        Intent[] intents = setTarget(target);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, intents,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Bitmap mLargeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("FCM Message")
                        .setContentText(message)
                        .setLargeIcon(mLargeIcon)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setVisibility(Notification.VISIBILITY_PRIVATE)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 안드로이드 8.0(오레오)부터는 한 개 이상의 알림 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_name);

            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private Intent[] setTarget(String target) {
        int targetByInt = Integer.parseInt(target);
        Intent[] intents = new Intent[targetByInt];
        Intent intent1 = new Intent(this, MainActivity.class);
        intents[0] = intent1;
        Intent intent2;
        Intent intent3;

        for (int i = 0; i < targetByInt; i++) {
            switch (i + 1) {
                case 2:
                    intent2 = new Intent(this, Main2Activity.class);
                    intents[i] = intent2;
                    break;
                case 3:
                    intent3 = new Intent(this, Main3Activity.class);
                    intents[i] = intent3;
                    break;
            }
            intents[i].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return intents;
    }
}
