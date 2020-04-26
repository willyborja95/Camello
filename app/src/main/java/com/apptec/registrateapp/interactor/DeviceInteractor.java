package com.apptec.registrateapp.interactor;

import com.apptec.registrateapp.models.Device;

import java.util.ArrayList;

public interface DeviceInteractor {

    /**
     *  The interface for the interactor
     */

    void getDevices();                            // Get the devices
    void showDevices(ArrayList<Device> devices);  // Call to the presenter

}
