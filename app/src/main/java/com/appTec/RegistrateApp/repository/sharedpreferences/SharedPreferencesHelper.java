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


    public static SharedPreferences getSharedPreferences(Context context){
        /**
         * Return a instance for the shared preferences using the context of the app.
         */
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);

            
        }

        return this.sharedPreferences;


    }

    SharedPreferences sharedPref = this.getSharedPreferences(
            Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_TOKEN, token);
        editor.commit();
}
