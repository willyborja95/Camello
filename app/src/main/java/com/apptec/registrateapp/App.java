package com.apptec.registrateapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.apptec.registrateapp.auth.AuthHelper;
import com.apptec.registrateapp.mainactivity.fhome.HandlerChangeWorkingStatus;
import com.apptec.registrateapp.mainactivity.fhome.geofence.GeofenceHelper;
import com.apptec.registrateapp.repository.localdatabase.DataGenerator;

public class App extends Application {

    /**
     * This class is used:
     * - For return an access to the application context from every part of the app.
     * - For provide the global methods to change work status
     * - Provide an AuthHelper for all the app
     */
    private static final String TAG = "App";

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

        // Populating database
        DataGenerator.prepopulateDatabase();
    }


    public static Context getContext(){
        /**
         * Be careful using this method only when you need the global application context. Do not use
         * it when you need a view context.
         */
        return App.context;
    }

    public static void changeWorkStatus() {
        /**
         * This method will be called when:
         * - The user press the 'finish work' button
         * - The user hang out from the work zone
         */
        new Thread(new HandlerChangeWorkingStatus()).start();

    }

    public static GeofenceHelper getGeofenceHelper() {
        /**
         * Return the singleton of geofence helper
         */
        return sGeofenceHelper;
    }

    public static AuthHelper getAuthHelper(){
        /**
         * Expose the auth helper
         */
        return App.sAuthHelper;
    }






}
