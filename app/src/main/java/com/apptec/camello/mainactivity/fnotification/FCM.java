package com.apptec.camello.mainactivity.fnotification;


import androidx.annotation.NonNull;

import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

public class FCM extends FirebaseMessagingService {
    /**
     * Class for manage the Firebase Messaging Service
     */


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("New message from: " + remoteMessage.getFrom());


        new Thread(new NotificationBuilder(remoteMessage)).start();


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
        Timber.d("New token generate by firebase: " + token);

        SharedPreferencesHelper.putStringValue(Constants.FIREBASE_TOKEN, token); // Saving on share preferences to use later


    }



}
