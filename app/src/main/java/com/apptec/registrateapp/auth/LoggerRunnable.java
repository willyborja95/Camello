package com.apptec.registrateapp.auth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.loginactivity.LoginProgress;
import com.apptec.registrateapp.mainactivity.fdevice.DeviceRetrofitInterface;
import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.UpdatePushTokenBody;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.apptec.registrateapp.repository.webservices.GeneralCallback;
import com.apptec.registrateapp.repository.webservices.pojoresponse.Error;
import com.apptec.registrateapp.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class LoggerRunnable implements Runnable {
    /**
     * This runnable will do the work for log in the user and
     * you can listen when all work is done
     * <p>
     * The process is describe in this flowchart:
     * https://app.diagrams.net/#G1PANL8t1ijf6I4f7RaZlSYtvjSb_KnJJb
     * If you do not have access to it. Contact Renato with the email renatojobal@gmail.com
     */

    private MutableLiveData<LoginProgress> loginResult; // This will be the listener got by the view model


    private LoginDataValidator data;


    public LoggerRunnable(
            MutableLiveData<LoginProgress> loginResult,
            LoginDataValidator data) {

        this.data = data.getValidData();


        this.loginResult = loginResult;
    }


    @Override
    public void run() {

        String localImei = getLocalImei();
        // At this point we already have the login response
        // We only need to compare the data.device and follow the flowchart

        if (data.device == null) {
            // Request device info
            // set "advise the user" to false
            // set "needs to claim this device" to true
            requestDeviceInfo(localImei, false, true);

        } else {
            // Then userImei = localImei ?

            if (SharedPreferencesHelper.getStringValue(Constants.CURRENT_IMEI, "None").equals(data.device.getIdentifier())) {
                // Update firebase token
                if (isTheSameFirebaseToken(data.device.getPushToken())) {
                    updateTheFirebaseToken(data.device.getPushToken(), data.device.getId());
                }
                // Login and set
                // set "advise the user" to false
                // set "needs to claim this device" to false
                login(data, false, false);

            } else {
                Timber.wtf("What just happen?");
                // Request device info
                // set "advise the user" to true
                // set "needs to claim this device" to true
                requestDeviceInfo(localImei, true, true);
            }

        }


    }


    public void saveUserAndCompany(UserModel user, CompanyModel company) {
        /**
         * This method will save the user into the database and the company
         * We are running in a new thread so we need directly
         */
        new Thread(() -> {
            RoomHelper.getAppDatabaseInstance().userDao().insertOrReplace(user);
            RoomHelper.getAppDatabaseInstance().companyDao().insertOrReplace(company);
        }).start();


    }

    public void requestDeviceInfo(String localImei, boolean adviseTheUser, @Nullable boolean needsToClaimThisDevice) {
        /**
         *
         * Here we request the information about this device.
         *
         * @param localImei = current phone's imei
         * @param userHasPreviousDevice = this boolean will be used for know if we have to send or
         *                              an advice to the user about losing his registration with his
         *                              previous phone
         * @param needsToClaimThisDevice = param to know if we should make the user register this device
         *                               by default it would be false ai this point and we need this variable
         *                               to send it to the login method
         *
         */
        Timber.d("Need to request device info");

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<GeneralResponse> call = deviceRetrofitInterface.getDeviceInfo(
                // Token:
                ApiClient.getAccessToken(),
                // IMEI:
                localImei
        );

        call.enqueue(
                new GeneralCallback<GeneralResponse>(call) {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        // If device is available
                        if (isDeviceAvailable(response.body())) {
                            // Login
                            login(data, adviseTheUser, needsToClaimThisDevice);
                        } else {
                            // Do not let the user login
                            doNotLetLogin();
                        }

                    }

                    private boolean isDeviceAvailable(GeneralResponse body) {
                        try {
                            Error error = body.getError();
                            if (error != null) {
                                Timber.d("This device is register by another person");
                                return false;
                            }
                        } catch (NullPointerException npe) {
                            Timber.d("This device is available to be claimed");

                        }
                        return true;

                    }
                }
        );


    }

    private String getLocalImei() {
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
                // The permission need to be granted first
            } else {
                if (android.os.Build.VERSION.SDK_INT >= 23 && android.os.Build.VERSION.SDK_INT < 26) {
                    imei = telephonyManager.getDeviceId();
                }
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    imei = telephonyManager.getImei();
                }
                Timber.d("Got IMEI");
            }
        }
        // Saving it on shared preferences
        SharedPreferencesHelper.putStringValue(Constants.CURRENT_IMEI, imei);
        Timber.d("IMEI: " + imei);
        return imei;

    }

    private boolean isTheSameFirebaseToken(String serverToken) {
        /**
         * Return true when the firebase token is the same in the server database
         */
        if (serverToken.equals(SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, ""))) {
            Timber.w("There firebase tokens are not the same");
            return false;

        }
        return true;
    }

    private void updateTheFirebaseToken(String firebaseToken, int deviceId) {
        /**
         * Upload to the server the new firebase token of this device
         */
        Timber.d("Init patch firebase token into the service");
        UpdatePushTokenBody updatePushTokenBody = new UpdatePushTokenBody();
        updatePushTokenBody.setPushToken(firebaseToken);

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<GeneralResponse<JsonObject>> call = deviceRetrofitInterface.updateFirebaseToken(
                // Token
                ApiClient.getAccessToken(),
                // Device id
                deviceId,
                // Body
                updatePushTokenBody
        );
        call.enqueue(new GeneralCallback<GeneralResponse<JsonObject>>(call) {
            @Override
            public void onResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                if (response.code() == 200) {
                    // Alright
                    Timber.i("Firebase token updated successful");

                } else {
                    Timber.e("Unexpected error while updating the firebase token. Here is the response" + response.body());
                    loginResult.postValue(new LoginProgress(R.string.title_error_connection, R.string.message_error_connection));
                }
            }
        });
    }


    private void login(LoginDataValidator data, boolean adviseTheUser, boolean needsToClaimThisDevice) {
        /**
         * We call this method when we finally verify the credentials
         *
         * Here:
         * - save user data in local database
         * - set shared preferences variables
         */

        /**
         * Save credentials
         */
        Timber.d("Login user.");
        Timber.d("Advise the user = " + adviseTheUser);
        Timber.d("needsToClamThisDevice = " + needsToClaimThisDevice);
        if (adviseTheUser) {
            // THis mean that we do not have to save the device data because the user has not register
            // this phone yet

            // TODO: Advise the user before login

        } else {
            // Save the data.device because the user already has register this phone
            new Thread(() -> {
                RoomHelper.getAppDatabaseInstance().deviceDao().insertOrReplace(data.device);
            }).start();

        }


        Timber.d("Saving data into local storage");
        saveUserAndCompany(data.user, data.company);             // Save the unique user into database

        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, data.accessToken);
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, data.refreshToken);
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
        SharedPreferencesHelper.putIntValue(Constants.CURRENT_USER_ID, data.user.getId());
        SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, needsToClaimThisDevice);


        loginResult.postValue(new LoginProgress(LoginProgress.SUCCESSFUL));

    }

    private void doNotLetLogin() {
        /**
         * We call this method when the user is trying to login
         * but this phone is claimed by another user
         */

        loginResult.postValue(new LoginProgress(R.string.device_already_taken_title, R.string.device_already_taken));
    }


}
