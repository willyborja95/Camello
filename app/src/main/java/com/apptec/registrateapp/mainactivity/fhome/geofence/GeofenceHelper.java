package com.apptec.registrateapp.mainactivity.fhome.geofence;

import com.apptec.registrateapp.App;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

public class GeofenceHelper {
    /**
     * This class will instance a geofence client
     *
     * @param sGeofencingList is a singleton
     */

    private static GeofencingClient sGeofencingClient;

    // Constructor
    public GeofenceHelper() {
        sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());
    }


    public static GeofencingClient getGeofencingClient() {
        /**
         * This method will return the global geofence client for the app.
         */
        // Verify is not null, otherwise gen an instance
        if (sGeofencingClient == null) {
            sGeofencingClient = LocationServices.getGeofencingClient(App.getContext());
        }
        return sGeofencingClient;
    }
}
