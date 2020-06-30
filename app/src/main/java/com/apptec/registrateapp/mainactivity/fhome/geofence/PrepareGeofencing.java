package com.apptec.registrateapp.mainactivity.fhome.geofence;


import android.app.PendingIntent;

import com.apptec.registrateapp.App;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import timber.log.Timber;

/**
 * Class runnable that will implements the settings for the location service
 * It will be like a builder for the {@link GeofenceHelper}
 */
public class PrepareGeofencing implements Runnable {


    private PendingIntent geofencePendingIntent;
    private GeofencingClient sGeofencingClient;
    private PrepareListener prepareListener;

    /**
     * Unique constructor
     *
     * @param geofencePendingIntent
     * @param sGeofencingClient
     * @param prepareListener
     */
    public PrepareGeofencing(PendingIntent geofencePendingIntent, GeofencingClient sGeofencingClient, PrepareListener prepareListener) {
        this.geofencePendingIntent = geofencePendingIntent;
        this.sGeofencingClient = sGeofencingClient;
        this.prepareListener = prepareListener;
    }

    // Listener
    public interface PrepareListener {

        // Method called when all work is done
        void onCompleteSetUp(GeofencingClient sGeofencingClient,
                             PendingIntent geofencePendingIntent);
    }

    /**
     *
     */
    @Override
    public void run() {
        Timber.d("Setting up the geofencing");
        geofencePendingIntent = null;
        sGeofencingClient = getGeofencingClient(sGeofencingClient);

        prepareListener.onCompleteSetUp(sGeofencingClient, geofencePendingIntent);
    }


    /**
     * This method will return the global geofence client for the app.
     */
    private GeofencingClient getGeofencingClient(GeofencingClient sGeofencingClient) {

        // Verify is not null, otherwise get an instance
        if (sGeofencingClient == null) {
            sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());

        }
        return sGeofencingClient;
    }

}
