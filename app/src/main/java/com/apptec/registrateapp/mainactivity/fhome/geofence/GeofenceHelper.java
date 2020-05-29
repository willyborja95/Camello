package com.apptec.registrateapp.mainactivity.fhome.geofence;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class GeofenceHelper {
    /**
     * This class will instance a geofence client
     *
     * @param sGeofencingList is a singleton
     */

    private final String TAG = GeofenceHelper.class.getSimpleName();

    private static GeofencingClient sGeofencingClient;

    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private PendingGeofenceTask pendingGeofenceTask = PendingGeofenceTask.NONE;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;


    // Constructor
    public GeofenceHelper() {
        sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());
    }


    public GeofencingClient getGeofencingClient() {
        /**
         * This method will return the global geofence client for the app.
         */
        // Verify is not null, otherwise gen an instance
        if (sGeofencingClient == null) {
            sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());

        }
        return sGeofencingClient;
    }

    public void setUpGeofencing() {
        /**
         * Set up the geofencing
         */
        Log.d(TAG, "Setting up the geofencing");
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        populateGeofenceList();
        sGeofencingClient = getGeofencingClient();
        addGeofences();

    }

    private void populateGeofenceList() {
        /**
         * First, use Geofence.Builder to create a geofence, setting the desired radius,
         * duration, and transition types for the geofence.
         *
         * Create as many geofences as company's work zones.
         */

        // Maybe the list of work zones could be obtained from other source
        List<WorkZoneModel> workZones = RoomHelper.getAppDatabaseInstance().workZoneDao().getListWorkZones();

        for (int i = 0; i < workZones.size(); i++) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(workZones.get(i).getId() + "")
                    .setCircularRegion(
                            Double.parseDouble(workZones.get(i).getLatitude()), // Latitude
                            Double.parseDouble(workZones.get(i).getLongitude()), // Longitude
                            GeofenceConstants.GEOFENCE_RADIUS_IN_METERS // Radius
                    )
                    .setExpirationDuration(GeofenceConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

    }


    private GeofencingRequest getGeofencingRequest() {
        /**
         * Use the GeofencingRequest class and its nested GeofencingRequestBuilder class to
         * specify the geofences to monitor and to set how related geofence events are triggered
         */
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceList);
        return builder.build();
    }


    private PendingIntent getGeofencePendingIntent() {
        /**
         * An Intent sent from Location Services can trigger various actions in your app,
         * but you should not have it start an activity or fragment, because components
         * should only become visible in response to a user action. In many cases, a
         * BroadcastReceiver is a good way to handle a geofence transition. A BroadcastReceiver
         * gets updates when an event occurs, such as a transition into or out of a geofence,
         * and can start long-running background work.
         */
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(App.getContext(), GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(App.getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private void addGeofences() {
        /**
         * To add geofences, use the GeofencingClient.addGeofences() method.
         * Provide the GeofencingRequest object, and the PendingIntent.
         */

        sGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        Log.d(TAG, "Geofences added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        Log.d(TAG, "Failed to add geofences");
                    }
                });
    }


    private void removeGeofences() {


        sGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // TODO:
            }
        });
    }


}
