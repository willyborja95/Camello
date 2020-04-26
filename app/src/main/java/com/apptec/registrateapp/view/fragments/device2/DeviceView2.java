package com.apptec.registrateapp.view.fragments.device2;

import com.apptec.registrateapp.models.Device;

import java.util.ArrayList;

public interface DeviceView2 {
    /**
     * Device View
     */

    void getDevices();                              // To the presenter
    void showDevices(ArrayList<Device> devices);    // From the presenter

}
