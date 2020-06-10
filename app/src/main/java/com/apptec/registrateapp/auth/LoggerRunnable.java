package com.apptec.registrateapp.auth;

import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.loginactivity.LoginResult;
import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

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
            SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);
            SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
            SharedPreferencesHelper.putIntValue(Constants.CURRENT_USER_ID, data.user.getId());


            if (data.device != null) {
                SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);
            } else {
                SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, true);
            }


            // Storage the user
            RoomHelper.getAppDatabaseInstance().userDao().insertOrReplace(data.user);

            // Storage the device (if it exist)
            if (data.device != null) {
                RoomHelper.getAppDatabaseInstance().deviceDao().insertOrReplace(data.device);
            }

            // Storage the company
            RoomHelper.getAppDatabaseInstance().companyDao().insertOrReplace(data.company);

            // Storage the work zones
            for (int i = 0; i < data.workZoneModels.size(); i++) {
                RoomHelper.getAppDatabaseInstance().workZoneDao().insertOrReplace(data.workZoneModels.get(i));
            }

            // Notify to hte view model that the process finished
            Timber.i("Login successfully");
            loginResult.postValue(new LoginResult(true));
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


}
