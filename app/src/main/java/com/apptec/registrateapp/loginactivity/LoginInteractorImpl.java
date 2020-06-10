package com.apptec.registrateapp.loginactivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.UserCredential;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.StaticData;
import com.apptec.registrateapp.repository.localdatabase.DatabaseAdapter;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.apptec.registrateapp.repository.webservices.pojoresponse.loginresponse.LoginResponse;
import com.apptec.registrateapp.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Deprecated
public class LoginInteractorImpl implements LoginInteractor {
    /**
     * Implementation of the interface
     */

    private static final String TAG = "LoginInteractor";

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
    public void verifyPreviousLogin() {
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

            loginPresenter.navigateToNextView();
        }
    }

    @Override
    public void handleLogin(UserCredential userCredential) {
        this.storageIMEI();
        LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);
        Call<LoginResponse> call = loginRetrofitInterface.loging2(userCredential);
        call.enqueue(new Callback<LoginResponse>() {


            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    Log.d(TAG, response.body().toString());

                    DeviceModel userDevice = response.body().getData().getDevice();
                    if (!isCorrectDevice(userDevice)) {
                        loginPresenter.doNotLetTheUserLogin();
                    } else {


                        int id = response.body().getData().getId();
                        String name = response.body().getData().getName();
                        String lastname = response.body().getData().getName();
                        String email = userCredential.getEmail();

                        // Getting the user
                        UserModel user = new UserModel();
                        user.setId(id);
                        user.setName(name);
                        user.setLastName(lastname);
                        user.setEmail(email);

                        // Getting the work zones
                        ArrayList<WorkZoneModel> workZoneArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().getWorkzones().size(); i++) {
                            workZoneArrayList.add(response.body().getData().getWorkzones().get(i));
                        }

                        // Getting the company
                        CompanyModel company = new CompanyModel();
                        company.setCompanyName(response.body().getData().getEnterprise());


                        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, response.body().getData().getTokens().getAccessToken().replace("\"", ""));
                        SharedPreferencesHelper.putIntValue(Constants.CURRENT_USER_ID, user.getId());
                        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
                        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);

                        if (response.body().getData().getDevice() != null) {
                            SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, false);
                        } else {
                            SharedPreferencesHelper.putBooleanValue(Constants.NEEDED_DEVICE_INFO, true);
                        }


                        StaticData.setCurrentUser(user);

                        // Storage thing into the database in a background thread
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                // Storage the user
                                RoomHelper.getAppDatabaseInstance().userDao().insert(user);

                                // Storage the device (if it exist)
                                if (response.body().getData().getDevice() != null) {
                                    RoomHelper.getAppDatabaseInstance().deviceDao().insert(response.body().getData().getDevice());
                                }

                                // Storage the company
                                RoomHelper.getAppDatabaseInstance().companyDao().insert(company);

                                // Storage the work zones
                                for (int i = 0; i < workZoneArrayList.size(); i++) {
                                    RoomHelper.getAppDatabaseInstance().workZoneDao().insert(workZoneArrayList.get(i));
                                }


                            }
                        }).start();


                        loginPresenter.hideLoginProgressDialog();
                        loginPresenter.navigateToNextView();

                    }


                } else if (response.code() == 404 || response.code() == 401) {
                    loginPresenter.hideLoginProgressDialog();
                    loginPresenter.showMessage("Autenticación fallida", "El usuario y contraseña proporcionados no son correctos.");

                } else if (response.code() == 502) {
                    loginPresenter.hideLoginProgressDialog();
                } else {
                    loginPresenter.hideLoginProgressDialog();
                    try {
                        JSONObject errorJson = new JSONObject(response.errorBody().string());
                        loginPresenter.showMessage("Error", errorJson.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginPresenter.hideLoginProgressDialog();
                loginPresenter.showMessage(App.getContext().getString(R.string.title_error_connection), App.getContext().getString(R.string.message_error_connection));

            }


        });
    }

    @Override
    public void handleFirstRun(Activity activity) {
        /**
         * When the app is running by first time or is reinstalled. But not when is updated.
         *
         * Ask for device permissions.
         * Change to false the flag of "is first running"
         */
        Log.w(TAG, "handleFirstRun: First run configurations");
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

    @Override
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
                Log.d(TAG, "Return false");
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
                Log.d(TAG, "Got IMEI");
            }
        }
        // Saving it on shared preferences
        SharedPreferencesHelper.putStringValue(Constants.CURRENT_IMEI, imei);
        Log.d(TAG, "IMEI: " + imei);

    }


}

