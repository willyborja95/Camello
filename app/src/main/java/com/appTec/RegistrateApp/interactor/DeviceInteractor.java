package com.appTec.RegistrateApp.interactor;

import com.appTec.RegistrateApp.models.Device;

import java.util.ArrayList;

public interface DeviceInteractor {

    /**
     *  The interface for the interactor
     */

    void getDevices();                            // Get the devices
    void showDevices(ArrayList<Device> devices);  // Call to the presenter

}
