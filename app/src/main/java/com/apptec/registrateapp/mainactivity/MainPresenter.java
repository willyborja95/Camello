package com.apptec.registrateapp.mainactivity;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

public class MainPresenter {
    /**
     * This class will help the main activity
     */


    public MainPresenter() {
        /**
         * Empty constructor
         */

    }


    public void initializeDeviceVerification(MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * This logic is explained in the flowchart:
         * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
         *
         * if you don't have access, contact Renato by email: renatojobal@gmail.com
         */

        // This flag will be changed to false when we have storage the device info
        boolean needed_device_info = SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.NEEDED_DEVICE_INFO, true);


        isNeedRegisterDevice.postValue(needed_device_info);

    }


}
