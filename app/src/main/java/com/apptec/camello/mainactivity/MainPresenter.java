package com.apptec.camello.mainactivity;

import androidx.lifecycle.MutableLiveData;

import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;

/**
 * This class will help the main activity
 */
public class MainPresenter {


    /**
     * Empty constructor
     */
    public MainPresenter() {
    }


    /**
     * This logic is explained in the flowchart:
     * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
     * <p>
     * if you don't have access, contact Renato by email: renatojobal@gmail.com
     *
     * @param isNeedRegisterDevice
     */
    public void initializeDeviceVerification(MutableLiveData<Boolean> isNeedRegisterDevice) {


        // This flag will be changed to false when we have storage the device info
        boolean needed_device_info = SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.NEEDED_DEVICE_INFO, true);


        isNeedRegisterDevice.postValue(needed_device_info);

    }


}
