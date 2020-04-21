package com.appTec.RegistrateApp.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.models.UserCredential;
import com.appTec.RegistrateApp.models.WorkzonesItem;
import com.appTec.RegistrateApp.presenter.LoginPresenterImpl;
import com.appTec.RegistrateApp.repository.StaticData;
import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.repository.localDatabase.RoomHelper;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.DeviceRetrofitInterface;
import com.appTec.RegistrateApp.repository.webServices.interfaces.LoginRetrofitInterface;
import com.appTec.RegistrateApp.repository.webServices.interfaces.PermissionTypeRetrofitInterface;
import com.appTec.RegistrateApp.repository.webServices.pojoresponse.loginresponse.LoginResponse;
import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractorImpl implements LoginInteractor {
    /**
     * Implementation of the interface
     */

    // Attributes
    LoginPresenterImpl loginPresenter; // Got as an attribute
    private static final String TAG = "LoginInteractor";

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
        LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);
        Call<LoginResponse> call = loginRetrofitInterface.login(userCredential);
        call.enqueue(new Callback<LoginResponse>() {


            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    Log.d(TAG, response.body().toString());

                    int id = response.body().getData().getId();
                    String name = response.body().getData().getName();
                    String lastname = response.body().getData().getName();
                    String email = userCredential.getEmail();


                    User user = new User();
                    user.setId(id);
                    user.setName(name);
                    user.setLastName(lastname);
                    user.setEmail(email);

                    // Getting the work zones
                    ArrayList<WorkzonesItem> workZoneArrayList = new ArrayList<>();
                    for(int i = 0; i < response.body().getData().getWorkzones().size(); i++){
                        workZoneArrayList.add(response.body().getData().getWorkzones().get(i));

                    }


                    Company company = new Company();
                    company.setCompanyName(response.body().getData().getEnterprise());


                    SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, response.body().getData().getTokens().getAccessToken().replace("\"", ""));


                    // DatabaseAdapter.getDatabaseAdapterInstance().insertUser(user);
                    // DatabaseAdapter.getDatabaseAdapterInstance().insertCompany(company);
                    StaticData.setCurrentUser(user);
//                    StaticData.getCurrentUser().setCompany(company);

                    RoomHelper.getAppDatabaseInstance().userDao().insert(user);

                    SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_WORKING, false);
                    SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, true);

                    loginPresenter.hideLoginProgressDialog();
                    loginPresenter.navigateToNextView();
                    //   findUserDevice();
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
                loginPresenter.showMessage("Error de conexión", "Al parecer no hay conexión a Internet.");

            }


        });
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


    /**
     * Searching if the device is registered
     */
    public void findUserDevice() {
        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Log.d("User ", StaticData.getCurrentUser().getId() + "");
        Call<JsonObject> deviceCall = deviceRetrofitInterface.get(ApiClient.getAccessToken(), StaticData.getCurrentUser().getId());
        Log.d("Tokeen", ApiClient.getAccessToken());
        deviceCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("deviceImei", "DEVICE ON RESPONSE OK");


                JsonArray deviceList = response.body().getAsJsonArray("data");

                boolean deviceFound = false;
                for (int i = 0; i < deviceList.size() && deviceFound == false; i++) {
                    JsonObject jsonDevice = deviceList.get(i).getAsJsonObject();

                    if (SharedPreferencesHelper.getSharedPreferencesInstance().getString(Constants.CURRENT_IMEI, "").equals(jsonDevice.get("imei").getAsString())) {
                        Device device = new Device();
                        int deviceId = jsonDevice.get("id").getAsInt();
                        String deviceName = jsonDevice.get("nombre").getAsString();
                        String deviceModel = jsonDevice.get("modelo").getAsString();
                        String deviceImei = jsonDevice.get("imei").getAsString();
                        boolean deviceStatus = jsonDevice.get("estado").getAsBoolean();
                        device.setId(deviceId);
                        device.setName(deviceName);
                        device.setModel(deviceModel);
                        device.setImei(deviceImei);
                        device.setStatus(deviceStatus);
                        DatabaseAdapter.getDatabaseAdapterInstance().insertDevice(device);
                        deviceFound = true;
                    }
                }
                findPermissionTypes();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    //Get PermissionType
    public void findPermissionTypes() {
        PermissionTypeRetrofitInterface permissionTypeRetrofitInterface = ApiClient.getClient().create(PermissionTypeRetrofitInterface.class);
        final Call<JsonObject> permissionTypeCall = permissionTypeRetrofitInterface.get(ApiClient.getAccessToken());
        permissionTypeCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonLstPermissionType = response.body().getAsJsonArray("data");
                if (jsonLstPermissionType.size() > 0) {
                    for (int i = 0; i < jsonLstPermissionType.size(); i++) {
                        JsonObject jsonPermissionType = jsonLstPermissionType.get(i).getAsJsonObject();
                        int permissionTypeId = jsonPermissionType.get("id").getAsInt();
                        String permissionTypeName = jsonPermissionType.get("nombre").getAsString();
                        PermissionType permissionType = new PermissionType(permissionTypeId, permissionTypeName);
                        DatabaseAdapter.getDatabaseAdapterInstance().insertPermissionType(permissionType);
                        StaticData.getPermissionTypes().add(permissionType);
                    }
                }
                loginPresenter.hideLoginProgressDialog();
                loginPresenter.navigateToNextView();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}

