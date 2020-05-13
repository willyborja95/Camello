package com.apptec.registrateapp;

import android.app.Application;
import android.content.Context;

import com.apptec.registrateapp.mainactivity.fhome.HandlerChangeWorkingStatus;

public class App extends Application {

    /**
     * This class is used:
     * - For return an access to the application context from every part of the app.
     * - For provide the global methods to change work status
     */

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
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





}
