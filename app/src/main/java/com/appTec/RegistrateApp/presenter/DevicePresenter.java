package com.appTec.RegistrateApp.presenter;

import com.appTec.RegistrateApp.models.Device;

import java.util.ArrayList;

public interface DevicePresenter {
    /**
     * Device Presenter
     * */

    void getDevices();                             // To the interactor
    void showDevices(ArrayList<Device> devices);   // From the interactor to the view

}
