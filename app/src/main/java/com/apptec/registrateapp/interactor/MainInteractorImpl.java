package com.apptec.registrateapp.interactor;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.repository.localDatabase.RoomHelper;
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


                // On response is ok = True
                try{
                    if (response.body().get("ok").getAsBoolean() == true) {


                        // Is the data null?
                        if (response.body().get("data") == null) {

                            // Case1: This device is not registered.

                            // Request information about this user devices

                            // The user has previous devices
                            if (true == false) {
                                // Case1.1: The user has another device.
                                // Advice thee user the previous devices will be replaced
                                // TODO: Advice
                            } else {
                                // Case1.2: The user has no other devices
                                // Continue
                            }

                            // Register new device
                            Log.d(TAG, "The user needs to register this device");
                            isNeedRegisterDevice.postValue(true);

                        } else {
                            // Case2: This device belong to this person.
//                                     The response is like this:
//                            "data": {
//                                "active": true,
//                                        "id": 6,
//                                        "name": "asds",
//                                        "model": "asddasd",
//                                        "identifier": "asasdasdsasdasdasdasdasdasdasdasd",
//                                        "pushToken": "asdaasdasdsadxxxxxxxxxxxxxxxxxxxxxxxxxxxassad",
//                                        "platform": 0,
//                                        "employeeId": 7
//                            }
                            Device device = new Device();

                            // TODO: This messy code cna be improved by creating a Gson Response
                            device.setId(Integer.parseInt(response.body().get("data").getAsJsonObject().get("id").toString()));
                            device.setName(response.body().get("data").getAsJsonObject().get("name").toString());
                            device.setModel(response.body().get("data").getAsJsonObject().get("model").toString());
                            device.setIdentifier(response.body().get("data").getAsJsonObject().get("identifier").toString());
                            device.setPushToken(SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, ""));
                            device.setPlatform(Integer.parseInt(response.body().get("data").getAsJsonObject().get("platform").toString()));


                            // The firebase tokens equals?
                            if (isTheSameFirebaseToken(response.body().get("data").getAsJsonObject().get("pushToken").toString())) {
                                // Case2.1: The firebase tokens are equals
                                // Continue with normal interaction

                            } else {
                                // Case2.2: The firebase tokens are not equals
                                // TODO: Update the firebase token into the server

                            }

                            // Save the device here in local database
                            RoomHelper.getAppDatabaseInstance().deviceDao().insert(device);
                        }


                    } else {
                        // Case3: The device is already used by another person
                        // TODO: Do not let the user interact

                    }


                    /** Change to false the flag of "is the first login" */

                    SharedPreferencesHelper.putBooleanValue(Constants.IS_THE_FIRST_LOGIN, false);
                }catch (NullPointerException npe){
                    Log.w(TAG, npe.getMessage());
                    /** Change to false the flag of "is the first login" */

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




    public void saveThisDevice(String name, String model, MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Method to save this device to the server
         */


        // Build the device object
        Device thisDevice = new Device();
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

                    isNeedRegisterDevice.postValue(false);      // Change the flag to the view model
                    RoomHelper.getAppDatabaseInstance().deviceDao().insert(thisDevice);

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO

            }
        });


    }


}
