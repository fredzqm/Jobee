package com.fredzqm.jobee.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fredzqm.jobee.LoginActivity;
import com.fredzqm.jobee.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "InstMessagingService";

    public MessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Map<String, String> data = remoteMessage.getData();
        Intent intent = new Intent(this, LoginActivity.class);
        for (String key : data.keySet()) {
            String x = data.get(key);
            intent.putExtra(key.substring(0, key.length()-1), x);
        }

        // launch a notification
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String x = intent.getStringExtra(RequestSender.TITLE);
        String y = intent.getStringExtra(RequestSender.BODY);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_start)
                .setContentTitle(intent.getStringExtra(RequestSender.TITLE))
                .setContentText(intent.getStringExtra(RequestSender.BODY))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_ALL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notification);
    }

//    public Notification getNotification(Intent intent) {
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle(getString(R.string.notification_title));
//        builder.setContentText(mPhotoMessage.getMessage());
//        builder.setSmallIcon(android.R.drawable.ic_menu_camera);
//        Bitmap thumbnail = Bitmap.createScaledBitmap(mBitmap, THUBNAIL_SIZE, THUBNAIL_SIZE, true);
//        builder.setLargeIcon(thumbnail);
//        int unusedRequestCode = 0;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, unusedRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        return builder.build();
//    }
}
