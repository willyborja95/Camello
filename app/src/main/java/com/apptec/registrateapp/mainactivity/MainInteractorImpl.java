package com.apptec.registrateapp.mainactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.mainactivity.fdevice.DeviceRetrofitInterface;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.models.UpdatePushTokenBody;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
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



    public void initializeDeviceVerification(MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * This logic is explained in the flowchart:
         * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
         *
         * if you don't have access, contact Renato by email: renatojobal@gmail.com
         */

        // This flag will be changed to false when we have storage the device info
        boolean needed_device_info = SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.NEEDED_DEVICE_INFO, true);
        if (needed_device_info) {
            handleFirstLogin(isNeedRegisterDevice);
        }

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

        // First we save the IMEI into shared preferences
        storageIMEI();

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
                        Log.d(TAG, "Ok = true");
                        // Is the data null?
                        if (response.body().get("data") == null) {
                            // Case1: This device is not registered.
                            // Request information about this user devices
                            // The user has previous devices
                            // TODO
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

                            // TODO: This messy code can be improved by creating a Gson Response
                            device.setId(Integer.parseInt(response.body().get("data").getAsJsonObject().get("id").toString()));
                            device.setName(response.body().get("data").getAsJsonObject().get("name").toString());
                            device.setModel(response.body().get("data").getAsJsonObject().get("model").toString());
                            device.setIdentifier(response.body().get("data").getAsJsonObject().get("identifier").toString());
                            device.setPushToken(SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, ""));
                            device.setPlatform(Integer.parseInt(response.body().get("data").getAsJsonObject().get("platform").toString()));

                            String serverFirebaseToken = response.body().get("data").getAsJsonObject().get("pushToken").toString();
                            // The firebase tokens equals?
                            if (isTheSameFirebaseToken(serverFirebaseToken)) {
                                // Case2.1: The firebase tokens are equals
                                // Continue with normal interaction
                            } else {
                                // Case2.2: The firebase tokens are not equals
                                updateTheFirebaseToken(serverFirebaseToken, device.getId());
                            }

                            // Save the device here in local database
                            Log.d(TAG, "Saving device into database");
                            RoomHelper.getAppDatabaseInstance().deviceDao().insert(device);
                        }


                    } else {
                        // Case3: The device is already used by another person
                        Log.d(TAG, "Ok = false");
                        // TODO: Do not let the user interact
                    }


                    /** Change to false the flag of "needed device info" */
                    SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);



                }catch (NullPointerException npe){
                    Log.w(TAG, npe.getMessage());
                    /** Change to false the flag of "needed device info" */
                    SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO: Handle this

            }
        });


    }

    public void storageIMEI() {
        /**
         *
         * Read the IMEI and storage it on an shared preferences's variable.
         * Change to false the flag of "is first running"
         */


        /** Read the IMEI and storage it on an shared preferences's variable. */
        TelephonyManager telephonyManager = (TelephonyManager) App.getContext().getSystemService(App.getContext().TELEPHONY_SERVICE);
        String imei = "";

        // Getting the imei
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (App.getContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // The permission eed to be granted first
            } else {
                if (android.os.Build.VERSION.SDK_INT >= 23 && android.os.Build.VERSION.SDK_INT < 26) {
                    imei = telephonyManager.getDeviceId();
                }
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    imei = telephonyManager.getImei();
                }
                Log.d(TAG, "Got IMEI");
            }
        }
        // Saving it on shared preferences
        SharedPreferencesHelper.putStringValue(Constants.CURRENT_IMEI, imei);
        Log.d(TAG, "IMEI: " + imei);

    }

    private boolean isTheSameFirebaseToken(String serverToken) {
        /**
         * Return true when the firebase token is the same in the server database
         */
        if (serverToken.equals(SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, ""))) {
            return false;
        }
        return true;
    }

    private void updateTheFirebaseToken(String firebaseToken, int deviceId) {
        /**
         * Upload to the server the new firebase token of this device
         */

        UpdatePushTokenBody updatePushTokenBody = new UpdatePushTokenBody();
        updatePushTokenBody.setPushToken(firebaseToken);

        // TODO:
        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.updateFirebaseToken(
                // Token
                ApiClient.getAccessToken(),
                // Device id
                deviceId,
                // Body
                updatePushTokenBody
        );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // All ok
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO: Handle this
            }
        });
    }


    public void saveThisDevice(String name, String model, MutableLiveData<Boolean> isNeedRegisterDevice) {
        /**
         * Method to save this device to the server
         */
        Log.d(TAG, "Save this device into the server.");

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

                Log.d(TAG, "Shot the API");
                Log.d(TAG, "Device: " + thisDevice.toString());
                // Change the flag
                if (response.isSuccessful()) {

                    isNeedRegisterDevice.postValue(false);      // Change the flag to the view model

                    // Delete other devices if the database is populated


                    Log.d(TAG, "Save this device information in the local database");
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
