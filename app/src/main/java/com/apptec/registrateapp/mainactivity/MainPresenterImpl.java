package com.apptec.registrateapp.mainactivity;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

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


    public void deleteSession() {
        /**
         * Delete the session from the user
         */
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, "");
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);


    }
}
