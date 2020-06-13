package com.apptec.registrateapp.mainactivity.fnotification;

public class NotificationBuilder implements Runnable {
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


    @Override
    public void run() {

    }
}
