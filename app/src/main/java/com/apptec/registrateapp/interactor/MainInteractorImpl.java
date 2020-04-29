package com.apptec.registrateapp.interactor;

import android.util.Log;

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
        if(previous_user_id == current_user_id){
            // That means that is the same user
            return true;
        }
        // Other wise return false
        return false;
    }


    public void handleFirstLogin(){
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
                ApiClient.getAccessToken(),
                SharedPreferencesHelper.getSharedPreferencesInstance().getString(Constants.CURRENT_IMEI, ""));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // TODO:
                Log.d(TAG, "Response boy: "+response.body());


                // Case1: This device is not registered.

                // Case1.1: The user has another device.
                // Case1.2: The user has no other devices


                // Case2: This device belong to this person.
                // Case2.1: The firebase tokens are equals
                // Case2.2: The firebase tokens are not equals


                // Case3: The device is already used by another person




            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO:

            }
        });


    }



    private boolean isTheSameFirebaseToken(){
        /**
         * Return true when the firebase token
         */
        // TODO
        return true;
    }

    private void updateTheFirebaseToken(String firebaseToken){
        /**
         * Upload to the server the new firebase token of this device
         */
        // TODO:
    }








}
