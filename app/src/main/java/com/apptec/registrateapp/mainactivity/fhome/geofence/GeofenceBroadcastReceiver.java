package com.apptec.registrateapp.mainactivity.fhome.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofenceTransitionsJobIntentService.enqueueWork(context, intent);

        // I pass this code to the GeofenceTransitionsJobIntentService
//        if(SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_USER_WORKING, false)){
//            SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
//
//            SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//            Calendar startDate = Calendar.getInstance();
//            SharedPreferencesHelper.putStringValue(Constants.LAST_EXIT_TIME, dateformat.format(startDate.getTime()));
//        }
    }


}
