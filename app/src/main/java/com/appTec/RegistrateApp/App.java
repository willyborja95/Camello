package com.appTec.RegistrateApp;

import android.app.Application;
import android.content.Context;

import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;

public class App extends Application {

    /**
     * This class is used oly for return an access to the application context from every part of the app.
     */

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public App() {
        instance = this;
    }

    public static Context getContext(){
        /**
         * Be careful using this method only when you need the global application context. Do not use
         * it when you need a view context.
         */
        return instance;
    }


}
