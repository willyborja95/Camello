package com.apptec.camello.repository.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.apptec.camello.App;
import com.apptec.camello.util.Constants;
/**
 * Class for provide a unique SharedPreferences instance for all the app
 */
public class SharedPreferencesHelper {


    private static SharedPreferences sharedPreferences;                      // Singleton

    /**
     * Return a instance for the shared preferences using the context of the app.
     */
    public static SharedPreferences getSharedPreferencesInstance() {

        if (sharedPreferences == null) {
            sharedPreferences = App.getContext().getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * Method to get a string value
     */
    public static String getStringValue(String key, String defaultValue) {

        return getSharedPreferencesInstance().getString(key, defaultValue);
    }

    /**
     * Method to save a string value
     */
    public static void putStringValue(String key, String value) {

        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Method to save an int value
     */
    public static void putIntValue(String key, int value) {

        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Method to save a boolean value
     */
    public static void putBooleanValue(String key, boolean value) {

        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Method to save a long value
     */
    public static void putLongValue(String key, Long value) {
        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putLong(key, value);
        editor.commit();
    }


}

