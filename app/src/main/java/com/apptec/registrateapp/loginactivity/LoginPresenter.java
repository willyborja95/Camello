package com.apptec.registrateapp.loginactivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.repository.StaticData;
import com.apptec.registrateapp.repository.localdatabase.DatabaseAdapter;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

import timber.log.Timber;

public class LoginPresenter {
    /**
     * This class wil do the hard work for login
     */


    public LoginPresenter() {
        /** Empty constructor */
    }


    public void verifyPreviousLogin(MutableLiveData<LoginResult> loginResultMutableLiveData) {
        /**
         * Verifying if credentials are saved
         */


        if (SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_USER_LOGGED, false)) { // If is a previous user logged

            DatabaseAdapter databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();

            StaticData.setCurrentUser(databaseAdapter.getUser());
            StaticData.setCurrentDevice(databaseAdapter.getDevice());
            StaticData.getCurrentUser().setCompany(databaseAdapter.getCompany());
            StaticData.getCurrentUser().setCompany(databaseAdapter.getCompany());
            StaticData.setPermissionTypes(databaseAdapter.getPermissionType());

            loginResultMutableLiveData.postValue(new LoginResult(true));
        }
    }

// TODO: hanldle login


    public void handleFirstRun(Activity activity) {
        /**
         * When the app is running by first time or is reinstalled. But not when is updated.
         *
         * Ask for device permissions.
         * Change to false the flag of "is first running"
         */
        Timber.w("handleFirstRun: First run configurations");
        /** Asking for device permissions*/
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    activity.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                        android.Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 225);
            }
        }

        /** Change to false the flag of "is first running" */
        SharedPreferencesHelper.putBooleanValue(Constants.IS_RUNNING_BY_FIRST_TIME, false);


    }


    public boolean isTheFirstRun() {
        /**
         * Method that returns true when the app is running by first time on the device.
         */
        return SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_RUNNING_BY_FIRST_TIME, true);
    }


    private boolean isCorrectDevice(DeviceModel userDevice) {
        /**
         * Check is the device's imei is the same with the user's device passes as param
         */
        if (userDevice != null) {
            String thisDeviceIMEI = SharedPreferencesHelper.getStringValue(Constants.CURRENT_IMEI, "");
            String userDeviceIMEI = userDevice.getIdentifier();

            if (!thisDeviceIMEI.equals(userDeviceIMEI)) {
                /**
                 * Case: Do not let the user login
                 */
                Timber.d("Return false");
                return false;
            }
        }
        return true;

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
                Timber.d("Got IMEI");
            }
        }
        // Saving it on shared preferences
        SharedPreferencesHelper.putStringValue(Constants.CURRENT_IMEI, imei);
        Timber.d("IMEI: " + imei);

    }

    public void handleLogin(MutableLiveData<LoginResult> loginResult) {
        /**
         * Call the server to verify credentials
         */
    }
}
