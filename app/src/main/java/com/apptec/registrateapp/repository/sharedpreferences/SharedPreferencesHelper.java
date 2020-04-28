package com.apptec.registrateapp.repository.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.util.Constants;

public class SharedPreferencesHelper {
    /**
     * Class for provide a unique SharedPreferences instance for all the app
     */


    private static SharedPreferences sharedPreferences;                      // Singleton
    private SharedPreferenceBooleanLiveData sharedPreferenceLiveData;        // Singleton of live data class


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



    public SharedPreferenceBooleanLiveData getSharedPreferenceForLiveData(){
        /**
         * Use this as follows:
         *
         * SharedPreferenceBooleanLiveData sharedPreferenceLiveData = SharedPreferencesHelper.getSharedPreferenceForLiveData();
         * sharedPreferenceLiveData.getBooleanLiveData(PreferenceKey.KEY_LOCATION_PERMISSION,false).observe(this,check->{
         *         if(check){
         *             setPermissionGranted(check);
         *         }
         *     });
         */

        return sharedPreferenceLiveData;
    }

    public void setSharedPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferencesInstance().edit();
        editor.putBoolean(key, value);
        editor.apply();
        sharedPreferenceLiveData = new SharedPreferenceBooleanLiveData(getSharedPreferencesInstance(),key,value);
    }


}

