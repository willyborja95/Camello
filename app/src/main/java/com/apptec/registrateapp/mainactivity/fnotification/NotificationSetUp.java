package com.apptec.registrateapp.mainactivity.fnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.apptec.registrateapp.App;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * This class wil set up the notifications channels
 */
public class NotificationSetUp implements Runnable {


    /**
     * Main method
     */
    @Override
    public void run() {
        // TODO:

    }


    private void createNotificationChannel(String channelId, int importance, String name, String description) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(App.getContext(), NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
