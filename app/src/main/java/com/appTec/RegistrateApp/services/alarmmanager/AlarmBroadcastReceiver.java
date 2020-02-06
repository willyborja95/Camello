package com.appTec.RegistrateApp.services.alarmmanager;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(AlarmBroadcastReceiver.class.getSimpleName(), "Time: " + new Date().getTime());
        // Hacer lo que necesites cada intervalo de tiempo con el alarm manager



    }
}
