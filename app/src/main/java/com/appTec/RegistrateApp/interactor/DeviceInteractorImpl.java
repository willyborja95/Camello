package com.appTec.RegistrateApp.interactor;

import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.presenter.DevicePresenterImpl;

import java.util.ArrayList;

public class DeviceInteractorImpl implements DeviceInteractor {

    /**
     * This class is the implementation of the interface for the interactor
     */

    // Attributes
    DevicePresenterImpl devicePresenter;

    public DeviceInteractorImpl(DevicePresenterImpl devicePresenter) {
        this.devicePresenter = devicePresenter;
    }

    @Override
    public void getDevices() {
        /** Get the devices from the repository */

    }

    @Override
    public void showDevices(ArrayList<Device> devices) {
        /** Call the presenter */

    }
}
