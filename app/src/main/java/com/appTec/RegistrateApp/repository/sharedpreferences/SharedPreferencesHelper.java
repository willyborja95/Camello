package com.appTec.RegistrateApp.services.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.appTec.RegistrateApp.util.Constants;

public class SharedPreferencesHelper {
    /**
     * Class for provide a unique SharedPreferences instance for all the app
     */

    // Singleton
    private static SharedPreferences sharedPreferences;


    public static SharedPreferences getSharedPreferencesInstance(Context context){
        /**
         * Return a instance for the shared preferences using the context of the app.
         */
        if(sharedPreferences == null){
            sharedPreferences = context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static String getStringValue(Context context, String key, String defaultValue) {
        /** Method to get a string value */
        return getSharedPreferencesInstance(context).getString(key, defaultValue);
    }

    public static void putStringValue(Context context, String key, String value) {
        /** Method to save a string value */
        final SharedPreferences.Editor editor = getSharedPreferencesInstance(context).edit();
        editor.putString(key , value);
        editor.commit();
    }

}
