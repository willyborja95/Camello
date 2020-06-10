package com.apptec.registrateapp.auth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.loginactivity.LoginResult;
import com.apptec.registrateapp.mainactivity.fdevice.DeviceRetrofitInterface;
import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.UpdatePushTokenBody;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.apptec.registrateapp.repository.webservices.GeneralCallback;
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
     */

    private MutableLiveData<LoginResult> loginResult; // This will be the listener got by the view model


    private LoginDataValidator data;


    public LoggerRunnable(
            MutableLiveData<LoginResult> loginResult,
            LoginDataValidator data) {

        this.data = data.getValidData();


        this.loginResult = loginResult;
    }


    @Override
    public void run() {
        try {

            saveUserAndCompany(data.user, data.company);             // Save the unique user into database

            Timber.d("Saving data into local storage");

            SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, data.accessToken);
            SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, data.refreshToken);

            if (data.device != null) {
                SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);

                if (isTheDeviceFromThisUser(data.device)) {
                    // The user is trying to login in another device, we
                    // Advise the user that he will lost his previous device

                    requestDeviceInfo();
                } else {
                    // The user is trying to login in his own device, so let him login
                    RoomHelper.getAppDatabaseInstance().deviceDao().insertOrReplace(data.device);             // Storage the device
                    if (!isTheSameFirebaseToken(data.device.getPushToken())) { // Updated the firebase token if needed
                        updateTheFirebaseToken(data.device.getPushToken(), data.device.getId());
                    }

                    // Storage the user
                    RoomHelper.getAppDatabaseInstance().userDao().insertOrReplace(data.user);


                    // Storage the company
                    RoomHelper.getAppDatabaseInstance().companyDao().insertOrReplace(data.company);

                    // Storage the work zones
                    for (int i = 0; i < data.workZoneModels.size(); i++) {
                        RoomHelper.getAppDatabaseInstance().workZoneDao().insertOrReplace(data.workZoneModels.get(i));
                    }


                    SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);
                    SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
                    SharedPreferencesHelper.putIntValue(Constants.CURRENT_USER_ID, data.user.getId());

                    // Notify to the view model the process finished
                    Timber.i("Login successfully");
                    loginResult.postValue(new LoginResult(true));

                }


            } else {
                requestDeviceInfo();
                SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, true);

            }


        } catch (NullPointerException npe) {
            Timber.e(npe);
        }
    }


    public void saveUserAndCompany(UserModel user, CompanyModel company) {
        /**
         * This method will save the user into the database and the company
         * We are running in a new thread so we need directly
         */

        RoomHelper.getAppDatabaseInstance().userDao().insertOrReplace(user);
        RoomHelper.getAppDatabaseInstance().companyDao().insertOrReplace(company);

    }

    public void requestDeviceInfo() {
        /**
         * This method is called from the MainActivity because at this point we will already have
         * the user data.
         *
         * Here we request the information about this device.
         *
         * The process is describe in this flowchart:
         * https://app.diagrams.net/#G1ECP8hY6_T7yfvYlv98ANLYkrdva3_54I
         * iI you do not have access to it. Contact Renato with the email renatojobal@gmail.com
         *
         *
         *
         */

        // First we save the IMEI into shared preferences
        storageIMEI();


        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<GeneralResponse<CheckDeviceResponse>> call = deviceRetrofitInterface.getDeviceInfo(
                // Token:
                ApiClient.getAccessToken(),
                // IMEI:
                SharedPreferencesHelper.getSharedPreferencesInstance().getString(Constants.CURRENT_IMEI, "")
        );


        call.enqueue(new GeneralCallback<GeneralResponse<CheckDeviceResponse>>(call) {
                         @Override
                         public void onResponse(Call<GeneralResponse<CheckDeviceResponse>> call, Response<GeneralResponse<CheckDeviceResponse>> response) {

                             try {
                                 if (response.body().isOk()) {  // On response is ok = True
                                     Timber.d("Ok = true");
                                     // Is the data null?
                                     if (response.body().getWrappedData() == null) {
                                         // Case1: This device is not registered.
                                         // So the user could register as device to himself,
                                         // but first we should verify has another device registered to
                                         // advise him he would lost his previous device

                                         // Request information about this user devices
                                         // If the user has previous devices
                                         if (data.device != null) {
                                             // Case1.1: The user has another device.
                                             // Advice thee user the previous devices will be replaced
                                             // TODO: Advice

                                         } else {
                                             // Case1.2: The user has no other devices
                                             // Continue
                                         }
                                         // Register new device
                                         Timber.d("The user needs to register this device");
                                         SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, true);
                                     } else {
                                         // Case2: This device belong to this person.
                                         // So we already save device info before and the user can login


                                     }


                                 } else {
                                     // Case3: The device is already used by another person
                                     Timber.d("Ok = false");
                                     // TODO: Do not let the user login

                                     loginResult.postValue(new LoginResult(R.string.device_already_taken_title, R.string.device_already_taken));
                                 }


                                 /** Change to false the flag of "needed device info" */
                                 SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);


                             } catch (NullPointerException npe) {
                                 Timber.w(npe.getMessage());
                                 /** Change to false the flag of "needed device info" */
                                 SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);
                             }
                         }
                     }


        );


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

        // TODO:
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
                    loginResult.postValue(new LoginResult(R.string.title_error_connection, R.string.message_error_connection));
                }
            }
        });
    }


    private boolean isTheDeviceFromThisUser(DeviceModel userDevice) {
        /**
         * Check if the device's imei is the same with the user's device passed as param
         */
        Timber.d("Checking if this device is available");
        if (userDevice != null) {
            String currentDeviceIMEI = SharedPreferencesHelper.getStringValue(Constants.CURRENT_IMEI, "Didn't found");
            String userDeviceIMEI = userDevice.getIdentifier();
            Timber.d("Current device IMEI: " + currentDeviceIMEI);
            Timber.d("User device IMEI " + userDeviceIMEI);
            if (!currentDeviceIMEI.equals(userDeviceIMEI)) {
                /**
                 * Case: Do not let the user login
                 *
                 * This means that user has a another device and is trying to log in this device but
                 * it is registered with another person
                 */
                Timber.d("Return false");
                return false;
            }
        }
        return true;

    }


}
