package com.fredzqm.jobee.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fredzqm.jobee.job_seeker.JobSeekerActivity;
import com.fredzqm.jobee.LoginActivity;
import com.fredzqm.jobee.R;
import com.fredzqm.jobee.recruiter.RecruiterActivity;
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
        Intent intent = new Intent();
        for (String key : data.keySet()) {
            String x = data.get(key);
            intent.putExtra(key.substring(0, key.length() - 1), x);
        }
        if (LoginActivity.getUserID() == null) {
            intent.setClass(this, LoginActivity.class);
        } else {
            switch (intent.getStringExtra(Notifier.NOTIF_TYPE)) {
                case Notifier.OFFER:
                case Notifier.REJECT:
                    intent.setClass(this, JobSeekerActivity.class);
                    break;
                case Notifier.ACCEPT_OFFER:
                    intent.setClass(this, RecruiterActivity.class);
                    break;
            }
        }

        // launch a notification
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_start)
                .setContentTitle(intent.getStringExtra(Notifier.TITLE))
                .setContentText(intent.getStringExtra(Notifier.BODY))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_ALL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notification);
    }

}
