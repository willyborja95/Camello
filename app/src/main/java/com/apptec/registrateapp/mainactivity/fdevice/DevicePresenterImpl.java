package com.apptec.registrateapp.mainactivity.fdevice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DevicePresenterImpl {
    /**
     * Device presenter
     *
     * This class will interact between viewmodel and repository
     */

    private final String TAG = "DevicePresenter";

    public DevicePresenterImpl() {

    }

    public LiveData<List<DeviceModel>> loadAllDevicesLiveData() {
        /**  Getting the device*/
        return RoomHelper.getAppDatabaseInstance().deviceDao().loadAllDevicesLiveData();
    }


    public void saveThisDevice(String name, String model, MutableLiveData<Boolean> isNeedRegisterDevice) {
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

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                // Change the flag
                if (response.isSuccessful()) {

                    isNeedRegisterDevice.postValue(false);      // Change the flag of the view model

                    // The room queries do not should be executed in the main thread, so we create a thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Timber.d("Save this device information in the local database");
                            RoomHelper.getAppDatabaseInstance().deviceDao().insertOrReplace(thisDevice);
                        }
                    }).start();


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO

            }
        });


    }
}
