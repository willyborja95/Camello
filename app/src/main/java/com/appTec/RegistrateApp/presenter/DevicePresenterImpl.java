package com.appTec.RegistrateApp.presenter;

import com.appTec.RegistrateApp.interactor.DeviceInteractor;
import com.appTec.RegistrateApp.interactor.DeviceInteractorImpl;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.device.DeviceView;

import java.util.ArrayList;

public class DevicePresenterImpl implements DevicePresenter {

    /**
     * Implementation of the interface.
     */

    // Attributes
    DeviceInteractorImpl deviceInteractor;
    DeviceView deviceView;

    public DevicePresenterImpl(DeviceView deviceView) {
        this.deviceView = deviceView;
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
