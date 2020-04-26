package com.apptec.registrateapp.presenter;

import com.apptec.registrateapp.interactor.DeviceInteractorImpl;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.view.fragments.device2.DeviceView2;

import java.util.ArrayList;

public class DevicePresenterImpl implements DevicePresenter {

    /**
     * Implementation of the interface.
     */

    // Attributes
    DeviceInteractorImpl deviceInteractor;
    DeviceView2 deviceView2;

    public DevicePresenterImpl(DeviceView2 deviceView2) {
        this.deviceView2 = deviceView2;
        this.deviceInteractor = new DeviceInteractorImpl(this);
    }

    @Override
    public void getDevices() {
        /** Call the interactor */

    }

    @Override
    public void showDevices(ArrayList<Device> devices) {
        /** Call the view */

    }
}
