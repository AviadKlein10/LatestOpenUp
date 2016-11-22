package aviv.myicebreaker;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import aviv.myicebreaker.activities.ActivityPrivateChat;
import aviv.myicebreaker.activities.MainActivity;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService2 extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private long DEFAULT_VIBRATION = 300L;
   private long[] pattern = {500, 250, 0 ,100,0, 250, 500};
    int numMessages = 0;
    int notificationID =1;

    private CharSequence msg = "tami says: hello \n tami says: hello  \n tami says: hello \n" +
            " tami says: hello  \n" +
            " tami says: hello\n" +
            " tami says: hello  \n" +
            " tami says: hello\n" +
            " tami says: hello  \n" +
            " tami says: hello";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional 
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
   //   sendNotification(remoteMessage.getNotification().getBody());
    //   sendNotification2(remoteMessage.getNotification().getBody());
  //   sendNotification3(remoteMessage.getNotification().getBody());
      sendNotification4(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
    }

    private void sendNotification4(String title,String body) {
        Log.i("Start", "notification");
//Log.d(title, body);
   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setContentTitle(title);
        mBuilder.setContentText("walla");
        mBuilder.setTicker("New Message Alert!");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

   /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);
        Log.d("numMessages ", numMessages +"");
   /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[6];
        events[0] = "This is first line....";
        events[1] = "This is second line...";
        events[2] = "This is third line...";
        events[3] = "This is 4th line...";
        events[4] = "This is 5th line...";
        events[5] = "This is 6th line...";

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, ActivityPrivateChat.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */

        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    private void sendNotification3(String body) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Sets an ID for the notification, so it can be updated
        int notifyID = 1;
       NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.mipmap.ic_launcher);
        int numMessages = 0;
// Start of a loop that processes data and then notifies the user

        mNotifyBuilder.setContentText(body)
                .setNumber(++numMessages);
        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(
                notifyID,
                mNotifyBuilder.build());
    }

    private void sendNotification2(String body) {
        long[] pattern = {500, 250, 0 ,100,0, 250, 500};

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Im the Title")
                        .setContentText(body)
                        .setVibrate(pattern)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
        /*
         * Sets the big view "big text" style and supplies the
         * text (the user's reminder message) that will be displayed
         * in the detail area of the expanded notification.
         * These calls are ignored by the support library for
         * pre-4.1 devices.
         */
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, builder.build());
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts 
    @TargetApi(Build.VERSION_CODES.M)
    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .addLine(formatInboxStyleLine("Alice", "hey there"))
                .addLine(formatInboxStyleLine("Bob", "what are you doing?"))
                .addLine(formatInboxStyleLine("Eve", "give me a call when you get a chance"))
                .addLine(formatInboxStyleLine("Trudy", "I like potatoes"))
                .addLine(formatInboxStyleLine("Mallory", "Dinner tomorrow?"));

        String msg = "fdsafds הרבה טקסט גדכגהדגהכגדה הכד כג רק רק הרקהד  ק' כרק רק' רק";

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Message")
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1,DEFAULT_VIBRATION,1,DEFAULT_VIBRATION})
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                    .setDeleteIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        Log.d("msg "," deleted");
        super.onDeletedMessages();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Spannable formatInboxStyleLine(String username, String message) {
        Spannable spannable = new SpannableString(username + " " + message);
        int color = getResources().getColor(R.color.colorPrimaryDark,null);
        spannable.setSpan(new ForegroundColorSpan(color), 0, username.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

}