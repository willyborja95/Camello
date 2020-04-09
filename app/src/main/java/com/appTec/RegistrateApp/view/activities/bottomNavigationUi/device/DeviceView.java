package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.device;

import com.appTec.RegistrateApp.models.Device;

import java.util.ArrayList;

public interface DeviceView {
    /**
     * Device View
     */

    void getDevices();                              // To the presenter
    void showDevices(ArrayList<Device> devices);    // From the presenter

}
