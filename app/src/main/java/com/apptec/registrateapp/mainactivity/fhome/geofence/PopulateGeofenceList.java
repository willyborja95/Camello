package com.apptec.registrateapp.mainactivity.fhome.geofence;

import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.google.android.gms.location.Geofence;

import java.util.List;

import timber.log.Timber;

/**
 * Thread that will populate the geofence list
 */
public class PopulateGeofenceList implements Runnable {

    private List<WorkZoneModel> workZones;
    private List<Geofence> geofenceList;

    // Listener
    PopulateListener populateListener;

    /**
     * UNique constructo
     *
     * @param workZones
     * @param geofenceList
     */
    public PopulateGeofenceList(List<WorkZoneModel> workZones, List<Geofence> geofenceList, PopulateListener populateListener) {
        this.workZones = workZones;
        this.geofenceList = geofenceList;
        this.populateListener = populateListener;
    }


    public interface PopulateListener {
        // Method that would implement the listener to save the geofencing list oin somewhere
        void onComplete(List<Geofence> geofencesList);
    }

    /**
     * Populate goefence list
     */
    @Override
    public void run() {
        Timber.d("Populating geofencing");
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
        populateListener.onComplete(geofenceList);
        Timber.d("Finished populating geofencing");
    }
}
