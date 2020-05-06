package com.apptec.registrateapp.presenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.interactor.MainInteractorImpl;
import com.apptec.registrateapp.models.WorkingPeriod;

public class MainPresenterImpl {
    /**
     * This class will help the main activity
     */

    MainInteractorImpl mainInteractor;

    public MainPresenterImpl() {
        /**
         * Empty constructor
         */
        mainInteractor = new MainInteractorImpl();
    }





    public void initializeDeviceVerification(MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Calling the interactor
         */
        mainInteractor.initializeDeviceVerification(isNeedRegisterDevice);
    }

    public void saveThisDevice(String name, String model, MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Calling the interactor
         */
        mainInteractor.saveThisDevice(name, model, isNeedRegisterDevice);
    }

    public void changeLastWorkingState(int intWorkingStatus) {
        /**
         * Calling the interactor
         */
        mainInteractor.changeLastWorkingState(intWorkingStatus);
    }

    public LiveData<WorkingPeriod> getLastWorkingPeriod() {
        /**
         * Calling the interactor
         */
        return mainInteractor.getLastWorkingPeriod();
    }
}
