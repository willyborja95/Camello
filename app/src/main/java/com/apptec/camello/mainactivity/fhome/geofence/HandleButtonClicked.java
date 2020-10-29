package com.apptec.camello.mainactivity.fhome.geofence;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.ActivityCompat;

import com.apptec.camello.App;
import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import timber.log.Timber;

/**
 * Class runnable that will calculate if the user is an company's
 * work zone otherwise will call the onNotAvailable method of the listener
 */
public class HandleButtonClicked implements Runnable {

    /**
     * Listener
     */
    private HandleButtonClicked.Listener listener;
    private Context context;
    private boolean booleanStartWorkig;


    // Client for get the location
    private FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(App.getContext());


    public interface Listener extends BaseProcessListener {
        /**
         * If the location permission are not granted or available
         */
        void onPermissionDenied();

    }


    /**
     * Constructor with listener
     */
    public HandleButtonClicked(@NotNull HandleButtonClicked.Listener listener, @NotNull Context context, boolean booleanStartWorkig) {
        this.listener = listener;
        this.context = context;
        this.booleanStartWorkig = booleanStartWorkig;
    }


    /**
     * Verify if the user is trying to stop or start
     * If trying to start:
     * 1. Verify if the has granted location permission
     * 2. Verify if the user has internet connection
     * 3. Verify if the user is inside a work zone
     */
    @Override
    public void run() {

        if (booleanStartWorkig) {
            /** We entry here when is the first time when the button is pressed since the app was installed.*/
            verifyAvailableStartWorking();
        } else {
            /** If the user is working now, he is trying to stop working */
            new Thread(new StopWorking(listener)).start();

        }


    }

    /**
     * If trying to start:
     * 1. Verify if the has granted location permission
     * 2. Verify if the user has internet connection
     * 3. Verify if the user is inside a work zone
     */
    private void verifyAvailableStartWorking() {
        if (permissionGranted()) {
            Timber.d("Location permission granted");
            if (hasInternet()) {
                Timber.d("Internet available");
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
                                        Timber.d("Distance: " + distance[0]);
                                        if (distance[0] <= workZones.get(i).getRadiusAsFloat()) { // If the user is inside the radius
                                            Timber.i("The user is inside a work zone");

                                            // Here we finally start working
                                            startWorking(workZones.get(i));
                                            return; // Break
                                        }

                                    }

                                    // If the user is not in any work zone
                                    Timber.w("The user is not in a work zone");
                                    listener.onErrorOccurred(R.string.not_correct_location_title, R.string.not_correct_location_message);
                                } else {
                                    Timber.e("The location is null");
                                }


                            }
                        });
            } else {
                Timber.d("No internet connection");
                listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
            }


        } else {
            Timber.d("Location permission denied");
            listener.onPermissionDenied();
        }

    }

    private void startWorking(WorkZoneModel workZoneModel) {
        new Thread(new StartWorking<Listener>(workZoneModel, listener)).start();
    }


    private boolean hasInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * @return true if the permission are granted
     */
    private boolean permissionGranted() {
        if (ActivityCompat.checkSelfPermission(
                context,
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
