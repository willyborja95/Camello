package com.apptec.registrateapp.mainactivity.fhome.geofence;

/**
 * Class runnable that will calculate if the user is an company's
 * work zone otherwise will call the onNotAvailable method of the listener
 */
public class OnAvailableLocation implements Runnable {

    /**
     * Listener
     */
    private OnAvailableLocation.Listener listener;


    public interface Listener {
        void onNotAvailableDevice();
    }


    /**
     * Constructor with listener
     */
    public OnAvailableLocation(OnAvailableLocation.Listener listener) {
        this.listener = listener;
    }


    /**
     * - Ask for current location
     */
    @Override
    public void run() {


    }
}
