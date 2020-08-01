package com.apptec.camello.auth;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.apptec.camello.R;
import com.apptec.camello.loginactivity.LoginProgress;
import com.apptec.camello.mainactivity.fdevice.DeviceRetrofitInterface;
import com.apptec.camello.models.CompanyModel;
import com.apptec.camello.models.UpdatePushTokenBody;
import com.apptec.camello.models.UserModel;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.Error;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * This runnable will do the work for log in the user and
 * you can listen when all work is done
 * <p>
 * The process is describe in this flowchart:
 * https://app.diagrams.net/#G1PANL8t1ijf6I4f7RaZlSYtvjSb_KnJJb
 * If you do not have access to it. Contact Renato with the email renatojobal@gmail.com
 */
public class LoggerRunnable implements Runnable {

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


        // At this point we already have the login response
        // We only need to compare the data.device and follow the flowchart

        if (data.device == null) {
            // Request device info
            // set "advise the user" to false
            // set "needs to claim this device" to true
            requestDeviceInfo(AuthHelper.getDeviceUniqueCode(), false, true);

        } else {
            // Then userImei = localImei ?

            if (AuthHelper.getDeviceUniqueCode().equals(data.device.getIdentifier())) {
                // Update firebase token if are not the same tokens
                if (!isTheSameFirebaseToken(data.device.getPushToken())) {
                    String newFirebaseToken = SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, "Error");
                    updateTheFirebaseToken(newFirebaseToken, data.device.getId());
                }
                // Login and set
                // set "advise the user" to false
                // set "needs to claim this device" to false
                login(data, false, false);

            } else {
                // Request device info
                // set "advise the user" to true
                // set "needs to claim this device" to true
                requestDeviceInfo(AuthHelper.getDeviceUniqueCode(), true, true);
            }

        }


    }

    /**
     * This method will save the user into the database and the company
     * We are running in a new thread so we need directly
     */
    public void saveUserAndCompany(UserModel user, CompanyModel company, List<WorkZoneModel> workZoneModels) {

        new Thread(() -> {
            RoomHelper.getAppDatabaseInstance().userDao().insertOrReplace(user);
            RoomHelper.getAppDatabaseInstance().companyDao().insertOrReplace(company);
            RoomHelper.getAppDatabaseInstance().workZoneDao().insertAllOrReplace(workZoneModels);
        }).start();


    }

    /**
     * Here we request the information about this device.
     *
     * @param uniqueDeviceCode       = current phone's imei or the key generated
     * @param adviseTheUser          = this boolean will be used for know if we have to send or
     *                               an advice to the user about losing his registration with his
     *                               previous phone
     * @param needsToClaimThisDevice = param to know if we should make the user register this device
     *                               by default it would be false ai this point and we need this variable
     *                               to send it to the login method
     */
    public void requestDeviceInfo(String uniqueDeviceCode, boolean adviseTheUser, @Nullable boolean needsToClaimThisDevice) {

        Timber.d("Need to request device info");

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<GeneralResponse> call = deviceRetrofitInterface.getDeviceInfo(
                // Token (We use the access token got from the login request because we not storage
                // it into the local storage yet, so we can use ApiClient.getAccessToken())
                data.accessToken,
                // IMEI:
                uniqueDeviceCode
        );

        call.enqueue(
                new GeneralCallback<GeneralResponse>(call) {

                    /**
                     * Method to be override by the classes that implement this method
                     *
                     * @param call     call
                     * @param response response
                     */
                    @Override
                    public void onFinalResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        // If device is available
                        if (isDeviceAvailable(response.body())) {
                            // Login
                            Timber.d("This device is available to be claimed");
                            login(data, adviseTheUser, needsToClaimThisDevice);
                        } else {
                            // Do not let the user login
                            Timber.w("This device is register by another person");
                            doNotLetLogin();
                        }
                    }

                    /**
                     * Simple method to know if the device is available
                     *
                     * @param body the body response
                     * @return true when the device is avaialble
                     */
                    private boolean isDeviceAvailable(GeneralResponse body) {
                        try {
                            Error error = body.getError();
                            if (error != null) {
                                return false;
                            }
                        } catch (NullPointerException npe) {

                        }
                        return true;

                    }
                }
        );


    }


    /**
     * Return true when the firebase token is the same in the server database
     */
    private boolean isTheSameFirebaseToken(String serverToken) {

        String realFirebaseToken = SharedPreferencesHelper.getStringValue(Constants.FIREBASE_TOKEN, "");

        if (serverToken.equals(realFirebaseToken)) {
            Timber.d("Firebase tokens are the same");
            return true;

        }
        Timber.w("The firebase tokens are not the same");
        return false;
    }

    /**
     * Upload to the server the new firebase token of this device
     */
    private void updateTheFirebaseToken(String firebaseToken, int deviceId) {

        Timber.d("Init patch firebase token into the service");
        Timber.d("New firebase token: " + firebaseToken);
        UpdatePushTokenBody updatePushTokenBody = new UpdatePushTokenBody();
        updatePushTokenBody.setPushToken(firebaseToken);

        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<GeneralResponse<JsonObject>> call = deviceRetrofitInterface.updateFirebaseToken(
                // Token (We use the access token got from the login request because we not storage
                // it into the local storage yet, so we can use ApiClient.getAccessToken())
                data.accessToken,
                // Device id
                deviceId,
                // Body
                updatePushTokenBody
        );
        call.enqueue(new GeneralCallback<GeneralResponse<JsonObject>>(call) {

            /**
             * Method to be override by the classes that implement this method
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                if (response.isSuccessful()) {
                    // Alright
                    Timber.i("Firebase token updated successful");
                } else {
                    Timber.e("Unexpected error while updating the firebase token. Here is the response: " + response
                            + " Here is the request body: " + updatePushTokenBody);
                }
            }
        });
    }


    /**
     * We call this method when we finally verify the credentials
     * <p>
     * Here:
     * - save user data in local database
     * - set shared preferences variables
     */

    private void login(LoginDataValidator data, boolean adviseTheUser, boolean needsToClaimThisDevice) {
        // Save credentials

        Timber.d("Login user.");
        Timber.d("Advise the user = " + adviseTheUser);
        Timber.d("needsToClamThisDevice = " + needsToClaimThisDevice);
        if (adviseTheUser) {
            // THis mean that we do not have to save the device data because the user has not register
            // this phone yet

            // TODO: Advise the user before login

        } else {
            if (!needsToClaimThisDevice) {
                // Save the data.device because the user already has register this phone
                new Thread(() -> {
                    SharedPreferencesHelper.putIntValue(Constants.CURRENT_DEVICE_ID, data.device.getId());
                    RoomHelper.getAppDatabaseInstance().deviceDao().insertOrReplace(data.device);
                }).start();
            }

        }


        Timber.d("Saving data into local storage");
        saveUserAndCompany(data.user, data.company, data.workZoneModels);             // Save the unique user into database

        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, data.accessToken);
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, data.refreshToken);
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
        SharedPreferencesHelper.putIntValue(Constants.CURRENT_USER_ID, data.user.getId());
        SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, needsToClaimThisDevice);


        loginResult.postValue(new LoginProgress(LoginProgress.SUCCESSFUL));

    }

    /**
     * We call this method when the user is trying to login
     * but this phone is claimed by another user
     */
    private void doNotLetLogin() {

        loginResult.postValue(new LoginProgress(R.string.device_already_taken_title, R.string.device_already_taken));
    }


}
