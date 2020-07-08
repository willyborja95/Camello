package com.apptec.camello.mainactivity.fdevice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apptec.camello.models.DeviceModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
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
        thisDevice.setActive(true);

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.registerDevice(
                // Token:
                ApiClient.getAccessToken(),
                // This device
                thisDevice
        );

        call.enqueue(new GeneralCallback<JsonObject>(call) {
            /**
             * Method that will be called after the onResponse default method after doing some validations
             * see {@link GeneralCallback}
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Change the flag
                if (response.isSuccessful()) {


                    // The room queries do not should be executed in the main thread, so we create a thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Timber.d("Save this device information in the local database");
                            RoomHelper.getAppDatabaseInstance().deviceDao().insertOrReplace(thisDevice);
                            isNeedRegisterDevice.postValue(false);      // Change the flag of the view model
                            SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);
                        }
                    }).start();


                }
            }
        });



    }
}
