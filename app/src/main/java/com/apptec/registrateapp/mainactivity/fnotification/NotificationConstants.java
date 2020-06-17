package com.apptec.registrateapp.mainactivity.fnotification;

import android.app.NotificationManager;

/**
 * Class for have the constants about the Notification module
 */
public class NotificationConstants {


    public static final String MESSAGES_CHANNEL_ID = "messages";
    public static final int MESSAGES_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;

    public static final String LOCATION_CHANNEL_ID = "location";
    public static final int LOCATION_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    /**
     * Payload node names for notifications
     */
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_MESSAGE = "message";
    public static final String NOTIFICATION_SENT_DATE = "createdAt";
    public static final String NOTIFICATION_EXPIRATION_DATE = "expiresAt";

}
