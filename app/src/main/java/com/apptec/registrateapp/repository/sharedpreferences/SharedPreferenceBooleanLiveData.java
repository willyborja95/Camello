package com.apptec.registrateapp.repository.sharedpreferences;

import android.content.SharedPreferences;

public class SharedPreferenceBooleanLiveData extends SharedPreferenceLiveData<Boolean>{
    /**
     * Using the abstract class to use with boolean values
     * @param prefs
     * @param key
     * @param defValue
     */

    public SharedPreferenceBooleanLiveData(SharedPreferences prefs, String key, Boolean defValue) {
        super(prefs, key, defValue);
    }

    @Override
    Boolean getValueFromPreferences(String key, Boolean defValue) {
        return sharedPrefs.getBoolean(key, defValue);
    }

}