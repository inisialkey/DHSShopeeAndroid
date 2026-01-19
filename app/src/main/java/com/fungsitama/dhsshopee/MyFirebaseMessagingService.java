package com.fungsitama.dhsshopee;

/**
 * Created by mohsy on 6/9/2016.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.fungsitama.dhsshopee.util.FCTApplication;
import com.fungsitama.dhsshopee.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="admin_channel";
    private Intent intent;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String status = remoteMessage.getNotification().getClickAction().toString();

        Log.d(FCTApplication.TAG, "onMessageReceived: "+ title+ " - "+message+ "- " + status);
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);

  /*
    Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
    to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
  */
        System.out.println("Sdk " + Build.VERSION.SDK_INT);
        Notification.Builder notificationBuilder = null;

        //perubahan-status (checkin, in-ra, hold-ra, reject-ra, load-ra,)
       /* if(status.equals("activity_tracking")){
            String activity = remoteMessage.getData().get("houseSmuNumber");
            intent = new Intent(this, ListTrackingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("vThouse",activity);
            intent.putExtras(bundle);
        }else if(status.equals("activity_invoice")){
            String activity = remoteMessage.getData().get("houseSmuNumber");
            intent = new Intent(this, ListInvoiceKlaimActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("HouseSmuNumber",activity);
            intent.putExtras(bundle);
        }else if (status.equals("activity_topup")){
            String activity = remoteMessage.getData().get("houseSmuNumber");
            intent = new Intent(this, TopUpActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
        }
        else{
            intent = new Intent(this, NavMenuActivity.class);
        }*/

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK );

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //setupChannels(notificationManager);
            CharSequence name = "New notification";
            String description = "Device to devie notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ADMIN_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

           notificationBuilder = new Notification.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_kisel)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setOnlyAlertOnce(false)
                    .setChannelId(ADMIN_CHANNEL_ID)
                    .setContentIntent(pendingIntent);


        } else {
            NotificationCompat.Builder nb = null;
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder = new Notification.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_logo_sigap)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(notificationSoundUri)
                        .setOnlyAlertOnce(false)
                        .setContentIntent(pendingIntent);
            }*/
            nb = new NotificationCompat.Builder(this,ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_kisel)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setOnlyAlertOnce(false)
                    .setContentIntent(pendingIntent);

        }
        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID,
                adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
/*    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        String click_action = remoteMessage.getNotification().getClickAction();
        Intent intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.mipmap.ic_logo_sigap);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

       *//* SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyFirebaseMessagingService.this);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(KEY_SHARED, vuser);
        ed.commit();*//*
        //Intent i = new Intent(MyFirebaseMessagingService.this, ListReqSPActivity.class);
        //startActivity(i);


        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }

    private void sendNotification(String title, String messageBody) {
        Intent[] intents = new Intent[1];
        Intent intent = new Intent(this, NavMenuActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Tittle", title);
        //intent.putExtra("ShortDesc", messageBody);
        intent.putExtra("Description", messageBody);
        intents[0] = intent;
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                intents, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri
                (RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationbuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_logo_sigap)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(BitmapFactory.decodeResource
                                (getResources(), R.mipmap.ic_logo_sigap));
        ;

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationbuilder.build());
    }*/
}
