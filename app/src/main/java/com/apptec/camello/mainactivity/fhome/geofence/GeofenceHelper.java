package com.apptec.camello.mainactivity.fhome.geofence;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.apptec.camello.App;
import com.apptec.camello.models.WorkZoneModel;
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

import timber.log.Timber;

/**
 * This class will instance a geofence client
 */
public class GeofenceHelper {

    // sGeofencingList is a singleton
    private GeofencingClient sGeofencingClient;

    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private PendingGeofenceTask pendingGeofenceTask = PendingGeofenceTask.NONE;
    private PendingIntent geofencePendingIntent;


    /**
     * Constructor that will prepare the geofences
     */
    public GeofenceHelper() {
        Timber.d("Geofence Helper constructor called");
        sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());
        new Thread(new PrepareGeofencing(geofencePendingIntent, sGeofencingClient, (sGeofencingClient, geofencePendingIntent) -> {
            this.sGeofencingClient = sGeofencingClient;
            this.geofencePendingIntent = geofencePendingIntent;
        })).start();
    }


    /**
     * Call a new thread to populate the geofence list
     */
    private List<Geofence> getGeofenceListWithOneWorkZone(WorkZoneModel workZone) {

        Timber.d("Populating geofencing");
        List<Geofence> geofenceList = new ArrayList<>();
        // Get target work zone from the
        geofenceList.add(new Geofence.Builder()
                .setRequestId(workZone.getId() + "")
                .setCircularRegion(
                        Double.parseDouble(workZone.getLatitude()), // Latitude
                        Double.parseDouble(workZone.getLongitude()), // Longitude
                        workZone.getRadiusAsFloat() // Radius
                )
                .setExpirationDuration(GeofenceConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        Timber.d("Finished populating geofencing");

        return geofenceList;
    }


    /**
     * Use the GeofencingRequest class and its nested GeofencingRequestBuilder class to
     * specify the geofences to monitor and to set how related geofence events are triggered
     */
    private GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
        Timber.d("getGeofencingRequest() ");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceList);
        return builder.build();
    }


    /**
     * An Intent sent from Location Services can trigger various actions in your app,
     * but you should not have it start an activity or fragment, because components
     * should only become visible in response to a user action. In many cases, a
     * BroadcastReceiver is a good way to handle a geofence transition. A BroadcastReceiver
     * gets updates when an event occurs, such as a transition into or out of a geofence,
     * and can start long-running background work.
     */
    private PendingIntent getGeofencePendingIntent() {
        Timber.d("getGeofencePendingIntent()");
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

    /**
     * To add geofences, use the GeofencingClient.addGeofences() method.
     * Provide the GeofencingRequest object, and the PendingIntent.
     */
    public void startTracking(WorkZoneModel workZoneModel) {
        Timber.i("Start to tracking");
        Timber.d("Start to tracking the work zone: " + workZoneModel.toString());
        sGeofencingClient.addGeofences(getGeofencingRequest(
                getGeofenceListWithOneWorkZone(workZoneModel)),
                getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        Timber.d("Geofences added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        Timber.d("Failed to add geofences");
                    }
                });
    }


    public void stopTracking() {
        Timber.i("Stop tracking");
        sGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // TODO:
            }
        });
    }


}
