package com.apptec.registrateapp.mainactivity.fnotification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.NotificationModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import timber.log.Timber;


/**
 * This class will create a notification and save the notification to database so it will be
 * showed on the notifications fragment.
 * <p>
 * This runnable could be called from the login activity, when the app is in background we have to
 * caught the notification from here.
 * <p>
 * Also it gould be called from the method onMessageReceived in the FMC broadcast receiver. This
 * method is called when the app is in foreground.
 */
public class NotificationBuilder implements Runnable {


    // Attributes
    Bundle extras;
    RemoteMessage remoteMessage;

    // This variable would be "true" when the runnable is called from the broadcast receiver
    // and "false" when is called from the LoginActivity onCreate()
    boolean isMessageReceivedInForeground;

    /**
     * This constructor is called for the login activity
     * Here we validate if a notification has data payload and save it into database.
     * In this way the login activity has no much work
     *
     * @param extras = Here there be the firebase message payload
     */
    public NotificationBuilder(@NonNull Bundle extras) {
        this.isMessageReceivedInForeground = false;

        this.extras = extras;

    }


    /**
     * This constructor is called by the broadcast receiver from Firebase Cloud Messaging
     *
     * @param remoteMessage = This is a firebase message object
     */
    public NotificationBuilder(@NonNull RemoteMessage remoteMessage) {
        this.isMessageReceivedInForeground = true;

        this.remoteMessage = remoteMessage;

    }


    /**
     * Main method
     */
    @Override
    public void run() {
        Timber.d("Notification builder running");
        NotificationModel notification;

        if (isMessageReceivedInForeground) {
            // Get the message from the remoteMessage object
            Timber.d("Searching for message received in foreground");
            notification = getNotificationFromRemoteMessage(remoteMessage);
        } else {
            // Get the message from the bundle extras
            Timber.d("Searching for message received in background");
            notification = getNotificationFromExtras(extras);

        }

        if (notification != null) {
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below
            sendNotification(notification);

            // Also save the notification into database
            saveNotificationIntoDatabase(notification);
        }


    }

    /**
     * Build the notification model
     *
     * @param extras = this.extras
     * @return NotificationModel instance
     */
    private NotificationModel getNotificationFromExtras(@NonNull Bundle extras) {
        Timber.d(extras.getClass().getName());
        NotificationModel targetNotification = new NotificationModel();


        for (String key : extras.keySet()) {
            Object value = extras.get(key);
            Timber.d("Key: " + key.getClass() + " Value: " + value.getClass());

        }
        return targetNotification;

    }

    /**
     * Build the notification model
     *
     * @param remoteMessage = this.remoteMessage
     * @return NotificationModel instance
     */
    private NotificationModel getNotificationFromRemoteMessage(@NonNull RemoteMessage remoteMessage) {
        NotificationModel targetNotification = new NotificationModel();


        try {
            Timber.d("Message data payload: " + remoteMessage.getData());


            // Get the data from the message payload
            Timber.d("Message title: " + remoteMessage.getData().get("title"));
            Timber.d("Message content: " + remoteMessage.getData().get("content"));
            Timber.d("Message sentDate: " + remoteMessage.getData().get("sentDate"));
            Timber.d("Message expirationDate: " + remoteMessage.getData().get("expirationDate"));

            String title = remoteMessage.getData().get("title");
            String content = remoteMessage.getData().get("content");
            Date sentDate = DateConverter.toDate(Long.parseLong(remoteMessage.getData().get("sentDate")));
            Date expirationDate = DateConverter.toDate(Long.parseLong(remoteMessage.getData().get("expirationDate")));

            targetNotification.setTitle(title);
            targetNotification.setText(content);
            targetNotification.setSentDate(sentDate.getTime());
            targetNotification.setExpirationDate(expirationDate.getTime());


        } catch (Exception e) {
            Timber.e(e);
        }


        return targetNotification;
    }


    /**
     * This method help us to validate the message or cancel the process before the app crashes
     *
     * @param remoteMessage = This is a firebase message object
     * @return true when the message is valid otherwise false
     */
    public boolean isValidMessageReceived(@NonNull RemoteMessage remoteMessage) {

        return true;
    }


    /**
     * Create a notification even if the app is in foreground
     *
     * @param notification = NotificationModel instance
     */
    public void sendNotification(@NonNull NotificationModel notification) {
        Timber.d("Creating the notification");
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext(), NotificationConstants.MESSAGES_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getText())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAllowSystemGeneratedContextualActions(true)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getContext());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());


    }


    /**
     * @param notification
     */
    public void saveNotificationIntoDatabase(@NonNull NotificationModel notification) {
        new Thread(
                () -> RoomHelper.getAppDatabaseInstance().notificationDao().insert(notification)
        ).run();
    }

}
