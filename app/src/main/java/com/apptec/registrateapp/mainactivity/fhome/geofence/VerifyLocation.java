package com.apptec.registrateapp.mainactivity.fhome.geofence;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import timber.log.Timber;

/**
 * Class runnable that will calculate if the user is an company's
 * work zone otherwise will call the onNotAvailable method of the listener
 */
public class VerifyLocation implements Runnable {

    /**
     * Listener
     */
    private VerifyLocation.Listener listener;


    // Client for get the location
    private FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(App.getContext());


    public interface Listener {
        /**
         * If the location permission are not granted or available
         */
        void onPermissionDenied();

        /**
         * If the user is not in a work zone
         */
        void onNotAvailableLocation();

        /**
         * If all is right, the permission has granted and the user is inside a work zone
         *
         * @param workZone is the work zone instance
         */
        void onAvailableLotion(WorkZoneModel workZone);       // If all is right
    }


    /**
     * Constructor with listener
     */
    public VerifyLocation(VerifyLocation.Listener listener) {
        this.listener = listener;
    }


    /**
     * - Ask for current location
     */
    @Override
    public void run() {

        if (permissionGranted()) {
            // Calculate if the user is inside a work zone
            Timber.d("Calculating if the user is inside a work zone");
            List<WorkZoneModel> workZones = RoomHelper.getAppDatabaseInstance().workZoneDao().getListWorkZones();


            // If the IDE underline the next line with red, do not worry, we are already checking if
            // the permissions are granted in the function permissionGranted()
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Timber.d("Got location: " + location.toString());
                                // Logic to handle location object
                                float[] distance = new float[2];
                                for (int i = 0; i < workZones.size(); i++) {
                                    Timber.d("Comparing with work zone: " + workZones.get(i));
                                    Location.distanceBetween(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            workZones.get(i).getLatitudeAsDouble(),
                                            workZones.get(i).getLongitudeAsDouble(),
                                            distance);
                                    Timber.d("Distance: " + distance.toString());
                                    if (distance[0] <= workZones.get(i).getRadiusAsFloat()) { // If the user is inside the radius
                                        Timber.i("The user is inside a work zone");
                                        listener.onAvailableLotion(workZones.get(i));
                                        return; // Break
                                    }

                                }

                                // If the user is not in any work zone
                                Timber.w("The user is not in a work zone");
                                listener.onNotAvailableLocation();
                            } else {
                                Timber.e("The location is null");
                            }


                        }
                    });


        } else {
            listener.onPermissionDenied();
        }


    }


    /**
     * @return true if the permission are granted
     */
    private boolean permissionGranted() {
        if (ActivityCompat.checkSelfPermission(
                App.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(App.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If the permission are not granted
            Timber.w("Permission about location are not granted");
            return false;
        }
        // If the permission are granted
        Timber.d("Permission about location granted");
        return true;
    }
}
