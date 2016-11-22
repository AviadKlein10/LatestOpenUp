package aviv.myicebreaker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import aviv.myicebreaker.activities.ActivityPrivateChat;
import aviv.myicebreaker.activities.MainActivity;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String NOTIFICATION_KEY = "notification_key2";
    private long DEFAULT_VIBRATION = 300L;
    private String sender, msg, chatId;
    private String newNotification;


    private String[] arrNotifications;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional 
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        sender = remoteMessage.getData().get("sender");
        chatId = remoteMessage.getData().get("chatId");
        msg = remoteMessage.getData().get("message");
        Log.d(TAG, "Notification key1: " + remoteMessage.getData().toString());
        String newNotification = sender + ": " + msg;
        receiveLastNotifications(newNotification);
        sendNotification();

    }

    private void receiveLastNotifications(String newNotification) {
        SharedPreferences sharedPreferences = getSharedPreferences(NOTIFICATION_KEY, MODE_PRIVATE);
        String previousNotifications = sharedPreferences.getString(NOTIFICATION_KEY, "");
        if (previousNotifications.length() > 0) {
            previousNotifications = newNotification + ", " + previousNotifications;
            convertPreviousNotiToArrayList(previousNotifications);

            Log.d("restoredNotification", previousNotifications + " ");
            //  globalLocalUser = JsonParser.parseUserToObject(restoredNotifications);
            saveLastNotification(previousNotifications);


        } else {

            saveLastNotification(newNotification);


            Log.d("no notifications", " login00");
        }
    }

    private void convertPreviousNotiToArrayList(String previousNotifications) {
        arrNotifications = previousNotifications.split(",");
        for (int i = 0; i < arrNotifications.length - 1; i++) {
            Log.d("arrN", arrNotifications[i] + "\n");
        }

    }

    private void sendNotification() {
        Intent resultIntent = new Intent(this, ActivityPrivateChat.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        String title = "OpenApp";
        String contentText = sender + ": " + msg;
        if (arrNotifications != null) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (int i = 0; i < arrNotifications.length; i++) {
                inboxStyle.addLine(arrNotifications[i]);
            }
            contentText = arrNotifications.length + " New messages";
            notificationBuilder.setStyle(inboxStyle);
        }


        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(Color.BLUE, 10000, 10)// TODO check we not working
                .setVibrate(new long[]{1, DEFAULT_VIBRATION, 1, DEFAULT_VIBRATION})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        Log.d("msg ", " deleted");
        super.onDeletedMessages();
    }

    private void saveLastNotification(String previousNotifications) {
        SharedPreferences sharedPref = getSharedPreferences(NOTIFICATION_KEY, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        if (previousNotifications != null) {
            editor.putString(NOTIFICATION_KEY, previousNotifications);
            Log.d("local notification ", previousNotifications + " ");
            editor.apply();
        }
    }



}