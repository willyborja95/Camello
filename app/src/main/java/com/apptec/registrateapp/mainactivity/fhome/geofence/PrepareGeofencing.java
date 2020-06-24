package com.apptec.registrateapp.mainactivity.fhome.geofence;


import android.app.PendingIntent;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Class runnable that will implements the settings for the location service
 * It will be like a builder for the {@link GeofenceHelper}
 */
public class PrepareGeofencing implements Runnable {


    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient sGeofencingClient;
    private PrepareListener prepareListener;

    /**
     * Unique constructor
     *
     * @param geofenceList
     * @param geofencePendingIntent
     * @param sGeofencingClient
     * @param prepareListener
     */
    public PrepareGeofencing(List<Geofence> geofenceList, PendingIntent geofencePendingIntent, GeofencingClient sGeofencingClient, PrepareListener prepareListener) {
        this.geofenceList = geofenceList;
        this.geofencePendingIntent = geofencePendingIntent;
        this.sGeofencingClient = sGeofencingClient;
        this.prepareListener = prepareListener;
    }

    // Listener
    public interface PrepareListener {

        // Method called when all work is done
        void onCompleteSetUp(List<Geofence> geofenceList,
                             GeofencingClient sGeofencingClient,
                             PendingIntent geofencePendingIntent);
    }

    /**
     *
     */
    @Override
    public void run() {
        Timber.d("Setting up the geofencing");
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        sGeofencingClient = getGeofencingClient(sGeofencingClient);
        populateGeofenceList(geofenceList);


        prepareListener.onCompleteSetUp(geofenceList, sGeofencingClient, geofencePendingIntent);
    }

    /**
     * Call a new thread to populate the geofence list
     *
     * @param geofenceList
     */
    private void populateGeofenceList(List<Geofence> geofenceList) {
        List<WorkZoneModel> workZones = RoomHelper.getAppDatabaseInstance().workZoneDao().getListWorkZones();

        new Thread(new PopulateGeofenceList(workZones, geofenceList, populatedGeofenceList -> {
            this.geofenceList = populatedGeofenceList;
        })).start();
    }


    /**
     * This method will return the global geofence client for the app.
     */
    public GeofencingClient getGeofencingClient(GeofencingClient sGeofencingClient) {

        // Verify is not null, otherwise get an instance
        if (sGeofencingClient == null) {
            sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());

        }
        return sGeofencingClient;
    }

}
