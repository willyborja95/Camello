package com.appTec.RegistrateApp.repository.webServices;


import android.util.Log;

import androidx.annotation.NonNull;

import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.repository.localDatabase.RoomHelper;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.repository.webServices.interfaces.TokenFirebaseInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * Class for manage the FCM
     */

    private final String TAG = "FCM";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
// ...

        // ToDo: Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

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

            Notification notification = new Notification(title, text, expirationDate, sentDate);


            /**
             * Saving it at database
             */
            RoomHelper.getAppDatabaseInstance().notificationDao().insert(notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }


    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // We want to send messages to this application instance
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }


    private void sendRegistrationToServer(String firebase_token){
        /**
         * Send the token to the server
         *
         * Save the token on shared preferences. Just in case something weird happen.
         */
        Log.d(TAG, "Trying to send the token to the server.");
        SharedPreferencesHelper.putStringValue(Constants.FIREBASE_TOKEN, firebase_token);

        TokenFirebaseInterface tokenFirebaseInterface = ApiClient.getClient().create(TokenFirebaseInterface.class);
        Call<JsonObject> call = tokenFirebaseInterface.post(ApiClient.getAccessToken(), firebase_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                /**
                 *
                 */

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                /**
                 * Save it on shared preferences to sent it later
                 */
                Log.d(TAG, "Call to service failed. The token cant be uploaded.");

            }
        });
    }

}
