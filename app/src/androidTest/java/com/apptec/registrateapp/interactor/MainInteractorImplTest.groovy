package com.apptec.registrateapp.interactor

import android.util.Log
import com.apptec.registrateapp.mainactivity.fdevice.DeviceRetrofitInterface
import com.apptec.registrateapp.models.DeviceModel
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper
import com.apptec.registrateapp.repository.webservices.ApiClient
import com.apptec.registrateapp.util.Constants
import com.google.gson.JsonObject
import org.junit.Test
import retrofit2.Call

class MainInteractorImplTest {

    private final TAG = MainInteractorImplTest.getClass().name;
    public static final long TEST_LONG = 12345678L;

    void testInitializeDeviceVerification() {
    }

    void testHandleFirstLogin() {
    }

    void testStorageIMEI() {
    }

    @Test
    void testSaveThisDevice() {
        /**
         * Method to save this device to the server
         */
        Log.d(TAG, "Save this device into the server.");

        // Build the device object
        DeviceModel thisDevice = new DeviceModel();
        thisDevice.setName(name);
        thisDevice.setModel(model);
        thisDevice.setIdentifier(SharedPreferencesHelper.getStringValue(Constants.CURRENT_IMEI, ""));
        thisDevice.setPushToken(SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, ""));

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.registerDevice(
                // Token:
                ApiClient.getAccessToken(),
                // This device
                thisDevice
        );


    }
}
