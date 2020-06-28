package com.apptec.registrateapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.apptec.registrateapp.auth.AuthHelper;
import com.apptec.registrateapp.mainactivity.fhome.geofence.GeofenceHelper;
import com.apptec.registrateapp.mainactivity.fnotification.NotificationSetUp;
import com.apptec.registrateapp.timber.DebugTree;
import com.apptec.registrateapp.timber.ReleaseTree;

import timber.log.Timber;

/**
 * This class is used:
 * - For return an access to the application context from every part of the app.
 * - For provide the global methods to change work status
 * - Provide an AuthHelper for all the app
 */
public class App extends Application {


    private static Context context;
    private static GeofenceHelper sGeofenceHelper;
    private static AuthHelper sAuthHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("RegisterApp", "onCreate called");
        App.context = getApplicationContext();
        sGeofenceHelper = new GeofenceHelper();
        sAuthHelper = new AuthHelper();


        // Setting up timber
        setUpTimber();


        // Call the notification setup. It does not matter if the channel is create twice, it don't have effect
        new Thread(new NotificationSetUp()).run();

    }

    /**
     * Timber is a library to log in a better way
     */
    private void setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
            Timber.i("Timber set up in DEBUG level");
        } else {
            Timber.plant(new ReleaseTree());
        }


    }


    /**
     * Be careful using this method only when you need the global application context. Do not use
     * it when you need a view context.
     */
    public static Context getContext() {
        return App.context;
    }

    /**
     * Let the geofence helper handle this
     */
    public static void changeWorkStatus() {
        getAuthHelper().changeWorkStatus();
    }

    /**
     * Return the singleton of geofence helper
     */
    public static GeofenceHelper getGeofenceHelper() {

        return sGeofenceHelper;
    }

    /**
     * Expose the auth helper
     */
    public static AuthHelper getAuthHelper() {
        return App.sAuthHelper;
    }


}
