package com.apptec.registrateapp.mainactivity.fhome.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("onReceive");
        GeofenceTransitionsJobIntentService.enqueueWork(context, intent);


    }


}
