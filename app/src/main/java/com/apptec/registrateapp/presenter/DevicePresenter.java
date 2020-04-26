package com.apptec.registrateapp.presenter;

import com.apptec.registrateapp.models.Device;

import java.util.ArrayList;

public interface DevicePresenter {
    /**
     * Device Presenter
     * */

    void getDevices();                             // To the interactor
    void showDevices(ArrayList<Device> devices);   // From the interactor to the view

}
