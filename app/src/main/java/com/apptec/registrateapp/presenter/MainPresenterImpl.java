package com.apptec.registrateapp.presenter;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.interactor.MainInteractorImpl;

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



    public boolean isTheFirstLogin(){
        /**
         * Calling the interactor
         */
        return mainInteractor.isTheFirstLogin();
    }

    public boolean isTheLoginFromTheSameUser(){
        /**
         * Calling the interactor
         */
        return mainInteractor.isTheLoginFromTheSameUser();
    }



    public void initializeDeviceVerification(MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Calling the interactor
         */
        mainInteractor.initializeDeviceVerification(isNeedRegisterDevice);
    }
}
