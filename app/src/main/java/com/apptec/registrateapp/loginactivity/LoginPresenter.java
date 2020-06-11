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


    public void verifyPreviousLogin(MutableLiveData<LoginProgress> loginResultMutableLiveData) {
        /**
         * Verifying if credentials are saved
         */


        if (SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.IS_USER_LOGGED, false)) { // If is a previous user logged
            Timber.d("User already logged");


            loginResultMutableLiveData.postValue(new LoginProgress(LoginProgress.SUCCESSFUL));
        }
    }


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

    public void handleLogin(MutableLiveData<LoginProgress> loginResult, String email, String password) {        /**
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
                if (response.code() == 200) {


                    // Get the data from the service here

                    Timber.d("Data response: " + response.body().getWrappedData().toString());

                    // Get the device first to validate and decide if we will continue the process
                    // or not
                    DeviceModel userDevice = response.body().getWrappedData().getDevice();


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


                } else if (response.code() == 404 || response.code() == 401) {
                    // Failed credentials
                    Timber.w("Invalid credentials");
                    loginResult.postValue(new LoginProgress(R.string.invalid_credentials_title, R.string.invalid_credentials));

                }

            }
        });


    }
}
