package com.appTec.RegistrateApp.view.fragments.device2;

import com.appTec.RegistrateApp.models.Device;

import java.util.ArrayList;

public interface DeviceView2 {
    /**
     * Device View
     */

    void getDevices();                              // To the presenter
    void showDevices(ArrayList<Device> devices);    // From the presenter

}
