package com.apptec.registrateapp.auth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.workers.retreshtoken.RefreshTokenWorker;
import com.apptec.registrateapp.util.Constants;

public class AuthHelper {
    /**
     * This class will contain auth stuff
     * <p>
     * - This class will login and logout the user
     * - Ask for permissions
     */

    // Work manager
    private WorkManager workManager = WorkManager.getInstance(App.getContext());


    public void login(UserModel user, CompanyModel company, String accessToken, String refreshToken) {
        /**
         * Save credentials
         */
        saveUserAndCompany(user, company);             // Save the unique user into database


        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, accessToken);
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, refreshToken);
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);

        this.initRefreshToken();
    }


    public void logout() {
        /**
         * Delete credentialss
         */

        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, "");
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, "");
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);


        deleteUserAndCompany();


    }

    public boolean imeiPermissionGranted(){
        // TODO
        return true;
    }

    public boolean locationPermissionGranted(){
        // TODO
        return true;
    }



    public boolean allPermissionsGranted() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (App.getContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    App.getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    App.getContext().checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }else {
                return false;
            }

        }
        return true;
    }


    private void initRefreshToken() {
        /**
         * This method we got a worker for refresh the token periodically
         */

        // Constraints: Do the work if the the network is connected
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a work request
        PeriodicWorkRequest refreshTokenRequest = new PeriodicWorkRequest.Builder(
                RefreshTokenWorker.class,
                Constants.ACCESS_TOKEN_EXPIRATION,
                Constants.ACCESS_TOKEN_EXPIRATION_UNIT)
                .setConstraints(constraints)
                .build();

        workManager.enqueueUniquePeriodicWork(
                "refresher",
                ExistingPeriodicWorkPolicy.REPLACE,
                refreshTokenRequest);

    }

    public void saveUserAndCompany(UserModel user, CompanyModel company) {
        /**
         * This method will save the user into the database and the company
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoomHelper.getAppDatabaseInstance().userDao().insert(user);
                RoomHelper.getAppDatabaseInstance().companyDao().insert(company);
            }
        }).start();
    }


    private void deleteUserAndCompany() {
        /**
         * Delete the logged user and company
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoomHelper.getAppDatabaseInstance().userDao().deleteAll();
                RoomHelper.getAppDatabaseInstance().companyDao().deleteAll();
            }
        }).start();
    }


}
