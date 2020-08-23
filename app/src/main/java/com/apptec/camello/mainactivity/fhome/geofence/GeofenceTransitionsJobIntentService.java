package com.apptec.camello.mainactivity.fhome.geofence;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.apptec.camello.R;
import com.apptec.camello.loginactivity.LoginActivity;
import com.apptec.camello.mainactivity.fnotification.NotificationConstants;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class GeofenceTransitionsJobIntentService extends JobIntentService {

    private static final int JOB_ID = 573;


    private static Context mContext;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Timber.d("Transition intent");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Timber.e(errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Register exit
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Timber.d("Transition exit at: " + DateConverter.toStringDateFormat(System.currentTimeMillis()));

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            for (int i = 0; i < triggeringGeofences.size(); i++) {
                Timber.d(triggeringGeofences.get(i).toString());
                triggeringGeofences.get(i).getRequestId();
            }

            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                    triggeringGeofences);
            Timber.d("Geofence transition details: " + geofenceTransitionDetails);
            // Send notification and log about the transition details.
            sendNotification(geofenceTransitionDetails);
            // Register current time as exit time
            Timber.i(geofenceTransitionDetails);
            Timber.d("Calling the global method to change the work status");

            new Thread(new StopWorking(null)).run(); // Stop working
        } else {
            // Log the error.
            Timber.e("Invalid transition");
        }
    }

    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private void sendNotification(String notificationDetails) {
        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // String formatedTime = DateConverter.toStringDateFormat(System.currentTimeMillis());

        //  String notificationMessage = getString(R.string.notification_exit_title)+", "+formatedTime;
        String notificationMessage = getString(R.string.notification_exit_title);

        Intent notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                NotificationConstants.LOCATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.notification_exit_title))
                .setContentIntent(notificationPendingIntent);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NotificationConstants.LOCATION_CHANNEL_ID); // Channel ID
        }

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }
}
