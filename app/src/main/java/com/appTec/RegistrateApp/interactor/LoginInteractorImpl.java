package com.appTec.RegistrateApp.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.appTec.RegistrateApp.presenter.LoginPresenterImpl;
import com.appTec.RegistrateApp.repository.StaticData;
import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.util.Constants;

import android.Manifest.permission;
import android.telephony.TelephonyManager;

public class LoginInteractorImpl implements LoginInteractor {
    /**
     * Implementation of the interface
     */

    // Attributes
    LoginPresenterImpl loginPresenter; // Got as an attribute


    // Constructor
    public LoginInteractorImpl(LoginPresenterImpl loginPresenter) {
        /**
         * Constructor
         */
        this.loginPresenter = loginPresenter;
    }


    @Override
    public void getInitialData(Activity activity) {
        /**
         * Get the data needed
         */
        // ToDo: Get tha data needed to the login case

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
    }

    @Override
    public void loadInitialData() {
        /**
         * Call the presenter
         */
        loginPresenter.loadInitialData();

    }

    @Override
    public void verifyPreviousLogin() {
        /**
         * Verifying if credentials are saved
         */


        if (getLoginUserStatus().equals(Constants.LOGGED_USER)) { // If is a previous user logged

            DatabaseAdapter databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();

            StaticData.setCurrentUser(databaseAdapter.getUser());
            StaticData.setCurrentDevice(databaseAdapter.getDevice());
            StaticData.getCurrentUser().setCompany(databaseAdapter.getCompany());
            StaticData.getCurrentUser().setCompany(databaseAdapter.getCompany());
            StaticData.setPermissionTypes(databaseAdapter.getPermissionType());

            loginPresenter.navigateToNextView();
        }
    }

    @Override
    public void handleLogin(String email, String password) {

    }

    @Override
    public void handleFirstRun(Activity activity) {
        /**
         * When the app is running by first time or is reinstalled. But not when is updated.
         *
         * Ask for device permissions.
         * Read the IMEI and storage it on an shared preferences's variable.
         * Change to false the flag of "is first running"
         */

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

        /** Read the IMEI and storage it on an shared preferences's variable. */
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        String imei = "";

        // Getting the imei
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
            } else {
                if (android.os.Build.VERSION.SDK_INT >= 23 && android.os.Build.VERSION.SDK_INT < 26) {
                    imei = telephonyManager.getDeviceId();
                }
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    imei = telephonyManager.getImei();
                }
            }
        }
        // Saving it on shared preferences
        SharedPreferencesHelper.putStringValue(Constants.CURRENT_IMEI, imei);

        /** Change to false the flag of "is first running" */
        SharedPreferencesHelper.putBooleanValue(Constants.APP_IS_RUNNING_BY_FIRST_TIME, false);

    }

    @Override
    public boolean isTheFirstRun() {
        /**
         * Method that returns true when the app is running by first time on the device.
         */
        return SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.APP_IS_RUNNING_BY_FIRST_TIME, true);
    }


    private String getLoginUserStatus() {
        return SharedPreferencesHelper.getStringValue(Constants.LOGIN_USER_STATE, "");
    }

}

