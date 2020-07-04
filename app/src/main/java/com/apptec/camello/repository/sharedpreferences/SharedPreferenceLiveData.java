package com.apptec.camello.repository.sharedpreferences;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

public abstract class SharedPreferenceLiveData<T> extends LiveData {
    /**
     * This abstract class could return a LiveData from a value
     */

    SharedPreferences sharedPrefs;
    String key;
    public T defValue;              // This value could be anyone that SharedPreferences could manage

    public SharedPreferenceLiveData(SharedPreferences prefs, String key, T defValue) {
        this.sharedPrefs = prefs;
        this.key = key;
        this.defValue = defValue;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (SharedPreferenceLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(key, defValue));
            }
        }
    };
    abstract T getValueFromPreferences(String key, T defValue);

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key, defValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }

    public SharedPreferenceLiveData<Boolean> getBooleanLiveData(String key, Boolean defaultValue) {
        /**
         * Call this method from a instance that extends from this abstract class to get Live Data
         */
        return new SharedPreferenceBooleanLiveData(sharedPrefs,key, defaultValue);
    }
}