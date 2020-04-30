package com.apptec.registrateapp.interactor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webServices.ApiClient;
import com.apptec.registrateapp.repository.webServices.interfaces.DeviceRetrofitInterface;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainInteractorImpl {
    /**
     * Interactor for Main Activity
     */
    private final String TAG = "MainInteractor";

    public boolean isTheFirstLogin() {
        /**
         * This method return true when is te first run from the user.
         */
        return SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_THE_FIRST_LOGIN, false);
    }

    public boolean isTheLoginFromTheSameUser() {
        /**
         * Return true when the user logged previous logged in this device.
         * Compare the previous user id
         */
        int previous_user_id = SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.PREVIOUS_LOGGED_USER_ID, 0);
        int current_user_id = SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.CURRENT_USER_ID, -1);
        if (previous_user_id == current_user_id) {
            // That means that is the same user
            return true;
        }
        // Other wise return false
        return false;
    }


    public void handleFirstLogin(MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * This method is called from the MainActivity because at this point we will already have
         * the user data.
         *
         * Here we request the information about this device.
         *
         * The process is describe in this flowchart:
         * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
         * iI you do not have access to it. Contact Renato with the email renatojobal@gmail.com
         *
         *
         *
         */


        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.getDeviceInfo(
                // Token:
                ApiClient.getAccessToken(),
                // IMEI:
                SharedPreferencesHelper.getSharedPreferencesInstance().getString(Constants.CURRENT_IMEI, "")

        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // TODO:
                Log.d(TAG, "Response body: " + response.body());
                Log.d(TAG, "Code: " + response.code());

                // On response is ok = True
                try{
                    if (response.body().get("ok").getAsBoolean() == true) {
                        Log.d(TAG, "Ok = false");

                        // Is the data null?
                        if (response.body().get("data") == null) {
                            Log.d(TAG, "Data = null");
                            // Case1: This device is not registered.

                            // TODO: Request information about the user devices

                            // The user has previous devices
                            if (true == false) {
                                // Case1.1: The user has another device.
                                // Advice thee user the previous devices will be replaced
                                // TODO: Advice
                            } else {
                                // Case1.2: The user has no other devices
                                // Continue
                            }

                            // TODO: Register new device
                            Log.d(TAG, "The user needs to register this device");
                            isNeedRegisterDevice.postValue(true);

                        } else {
                            // Case2: This device belong to this person.
                            // The firebase tokens equals?
                            if (isTheSameFirebaseToken(response.body().get("data").getAsJsonObject().get("pushToken").toString())) {
                                // Case2.1: The firebase tokens are equals
                                // Continue with normal interaction

                            } else {
                                // Case2.2: The firebase tokens are not equals
                                // TODO: Update the firebase token
                            }
                        }


                    } else {
                        // Case3: The device is already used by another person
                        // TODO: Do not let the user interact

                    }


                    /** Change to false the flag of "is the first login" */
                    // This line is commented while we are testing (Do not delete)
                    SharedPreferencesHelper.putBooleanValue(Constants.IS_THE_FIRST_LOGIN, false);
                }catch (NullPointerException npe){
                    Log.w(TAG, npe.getMessage());
                    /** Change to false the flag of "is the first login" */
                    // This line is commented while we are testing (Do not delete)
                    SharedPreferencesHelper.putBooleanValue(Constants.IS_THE_FIRST_LOGIN, false);
                }




            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO:

            }
        });


    }


    private boolean isTheSameFirebaseToken(String serverToken) {
        /**
         * Return true when the firebase token
         */
        // TODO
        return true;
    }

    private void updateTheFirebaseToken(String firebaseToken) {
        /**
         * Upload to the server the new firebase token of this device
         */
        // TODO:
    }


    public void initializeDeviceVerification(MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Calling the presenter
         * This logic is explained in the flowchart:
         * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
         *
         * if you don't have access, contact Renato by email: renatojobal@gmail.com
         */
        if (isTheFirstLogin()) {
            // return true;
            handleFirstLogin(isNeedRegisterDevice);
        } else if (!isTheLoginFromTheSameUser()) {
            // return true;
            handleFirstLogin(isNeedRegisterDevice);
        }
        // return false;

    }

    public void saveThisDevice(String name, String model, MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Method to save this device to the server
         */
        // TODO:

        // Build the device object
        Device device = new Device();
        device.setImei(SharedPreferencesHelper.getStringValue(Constants.CURRENT_IMEI, ""));
        device.setModel(model);
        device.setName(name);
        device.setPushToken(SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, ""));

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.post(
                // Token:
                ApiClient.getAccessToken(),
                // This device
                device
        );

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // TODO
                Log.d(TAG, "Response "+response.body());


                // Change the flag
                isNeedRegisterDevice.postValue(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO

            }
        });


    }
}
