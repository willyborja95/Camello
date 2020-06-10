package com.apptec.registrateapp.loginactivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.auth.LoggerRunnable;
import com.apptec.registrateapp.auth.LoginDataValidator;
import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.UserCredential;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.StaticData;
import com.apptec.registrateapp.repository.localdatabase.DatabaseAdapter;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.apptec.registrateapp.repository.webservices.GeneralCallback;
import com.apptec.registrateapp.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.registrateapp.util.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
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


    private boolean isAvailableDevice(DeviceModel userDevice) {
        /**
         * Check if the device's imei is the same with the user's device passed as param
         */

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

    public void handleLogin(MutableLiveData<LoginResult> loginResult, String email, String password) {
        /**
         * Call the server to verify credentials
         */
        Timber.d("Handling login process");

        this.storageIMEI();

        LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);

        // Creates the body
        UserCredential userCredential = new UserCredential(email, password);


        Call<GeneralResponse<LoginDataResponse>> call = loginRetrofitInterface.login(userCredential);

        Timber.d("Sending credentials to the server");
        call.enqueue(new GeneralCallback<GeneralResponse<LoginDataResponse>>(call) {
            @Override
            public void onResponse(Call<GeneralResponse<LoginDataResponse>> call, Response<GeneralResponse<LoginDataResponse>> response) {
                // Get the data from the service here

                Timber.d("Data response: " + response.body().getWrappedData().toString());

                // Get the device first to validate and decide if we will continue the process
                // or not
                DeviceModel userDevice = response.body().getWrappedData().getDevice();

                if (!isAvailableDevice(userDevice)) {
                    // ! Do not let the user interact
                    Timber.w("This device is used by another person");
                    loginResult.postValue(new LoginResult(R.string.device_already_taken));
                } else {

                    Timber.i("This device is available");
                    // Getting the user
                    UserModel user = new UserModel();
                    user.setId(response.body().getWrappedData().getId());
                    user.setName(response.body().getWrappedData().getName());
                    user.setLastName(response.body().getWrappedData().getName());
                    user.setEmail(userCredential.getEmail());

                    // Getting the work zones
                    ArrayList<WorkZoneModel> workZoneArrayList = new ArrayList<>();
                    for (int i = 0; i < response.body().getWrappedData().getWorkzones().size(); i++) {
                        workZoneArrayList.add(response.body().getWrappedData().getWorkzones().get(i));
                    }

                    // Getting the company
                    CompanyModel company = new CompanyModel();
                    company.setCompanyName(response.body().getWrappedData().getEnterprise());


                    // Getting the tokens
                    String accessToken = response.body().getWrappedData().getTokens().getAccessToken().replace("\"", "");
                    String refreshToken = response.body().getWrappedData().getTokens().getRefreshToken().replace("\"", "");

                    // Build the data holder for send it to the runnable
                    LoginDataValidator loginDataValidator = new LoginDataValidator(
                            user,
                            company,
                            userDevice,
                            workZoneArrayList,
                            accessToken,
                            refreshToken);

                    // Storage all
                    new Thread(new LoggerRunnable(loginResult, loginDataValidator)).start();


                }

            }
        });


    }
}
