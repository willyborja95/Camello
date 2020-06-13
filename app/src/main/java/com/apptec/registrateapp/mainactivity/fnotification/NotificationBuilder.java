package com.apptec.registrateapp.mainactivity.fnotification;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.apptec.registrateapp.models.NotificationModel;
import com.google.firebase.messaging.RemoteMessage;

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
        if (isMessageReceivedInForeground) {
            // Get the message from the remoteMessage object
            Timber.d("Searching for message received in foreground");
            NotificationModel notification = getNotificationFromRemoteMessage(remoteMessage);
        } else {
            // Get the message from the bundle extras
            Timber.d("Searching for message received in background");
            NotificationModel notification = getNotificationFromExtras(extras);

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


    }

}
