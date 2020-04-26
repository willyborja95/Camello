package com.apptec.registrateapp.repository.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofenceTransitionsJobIntentService.enqueueWork(context, intent);
//        if(getCurrentWorkingState(context).equals(Constants.STATE_WORKING)){
//            changeWorkingState(Constants.STATE_NOT_WORKING, context);
//        }
        if(SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_USER_WORKING, false)){
            SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);

            SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Calendar startDate = Calendar.getInstance();
            SharedPreferencesHelper.putStringValue(Constants.LAST_EXIT_TIME, dateformat.format(startDate.getTime()));
        }
    }

//    private void changeWorkingState(String state, Context context) {
//        Log.d("geof", "ejecutandose geofencing");
//        Log.d("sync", "cambiando el working state");
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(Constants.CURRENT_STATE, state);
//        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//        Log.d("log", "hora registrada");
//        final Calendar startDate = Calendar.getInstance();
//        Log.d("log",  dateformat.format(startDate.getTime()));
//        editor.putString(Constants.LAST_EXIT_TIME, dateformat.format(startDate.getTime()));
//        editor.putString("test", "Hola willy "+dateformat.format(startDate.getTime()));
//        editor.commit();
//    }

//    private String getCurrentWorkingState(Context context) {
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
//        return sharedPref.getString(Constants.CURRENT_STATE,"");
//    }
}
