package com.appTec.RegistrateApp.repository.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.appTec.RegistrateApp.App;
import com.appTec.RegistrateApp.util.Constants;

public class SharedPreferencesHelper {
    /**
     * Class for provide a unique SharedPreferences instance for all the app
     */

    // Singleton
    private static SharedPreferences sharedPreferences;


    public static SharedPreferences getSharedPreferencesInstance(){
        /**
         * Return a instance for the shared preferences using the context of the app.
         */
        if(sharedPreferences == null){
            sharedPreferences = App.getContext().getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static String getStringValue(String key, String defaultValue) {
        /** Method to get a string value */
        return getSharedPreferencesInstance().getString(key, defaultValue);
    }

    public static void putStringValue(String key, String value) {
        /** Method to save a string value */
        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putString(key , value);
        editor.commit();
    }

    public static void putBooleanValue(String key, boolean value){
        /** Method to save a boolean value */
        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
