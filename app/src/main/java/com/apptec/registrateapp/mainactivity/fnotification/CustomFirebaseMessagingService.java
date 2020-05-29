package com.apptec.registrateapp.mainactivity.fnotification;


import android.util.Log;

import androidx.annotation.NonNull;

import com.apptec.registrateapp.models.NotificationModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * Class for manage the FCM
     */

    private final String TAG = "FCM";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "New message from: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData().getClass());
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Notification title: " + remoteMessage.getNotification().getTitle());

            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();
            Map map = remoteMessage.getData();

            Log.d(TAG, "expirationDate: " + map.get("expirationDate").toString());
            Log.d(TAG, "sentDate" + map.get("sentDate").toString());


            Date expirationDate =  new Date(Long.parseLong(map.get("expirationDate").toString())*1000);
            Date sentDate = new Date(Long.parseLong(map.get("sentDate").toString())* 1000);

            NotificationModel notification = new NotificationModel(title, text, expirationDate, sentDate);


            /**
             * Saving it at database
             */
            RoomHelper.getAppDatabaseInstance().notificationDao().insert(notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below
    }


    @Override
    public void onNewToken(@NonNull String token) {
        /**
         * This method is called in the first run. Even before the user login. So you could save this
         * token somewhere and send to the server after the login.
         *
         * This method is called when the data of the app is deleted by the user from the settings. So
         * the server could handle a token actualization.
         */
        Log.d(TAG, "New token generate by firebase: " + token);

        SharedPreferencesHelper.putStringValue(Constants.FIREBASE_TOKEN, token); // Saving on share preferences to use later


    }


}
