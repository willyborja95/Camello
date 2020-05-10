package com.apptec.registrateapp.mainactivity.fdevice;

import com.apptec.registrateapp.models.Device;

import java.util.ArrayList;

@Deprecated
public interface DeviceView2 {
    /**
     * Device View
     * This is not used anymore
     * @deprecated
     */

    void getDevices();                              // To the presenter

    void showDevices(ArrayList<Device> devices);    // From the presenter

}
