package com.appTec.RegistrateApp.view;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.models.UserCredential;
import com.appTec.RegistrateApp.models.WorkingPeriod;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.DeviceRetrofitInterface;
import com.appTec.RegistrateApp.services.webServices.interfaces.LoginRetrofitInterface;
import com.appTec.RegistrateApp.services.webServices.interfaces.PermissionTypeRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.Manifest.permission;


import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener {

    //UI elements
    private ImageButton btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;
    ProgressDialog progressDialog;

    //Business logic elements
    private String email;
    private String password;
    private String deviceImei;
    private Device device;
    private User user;
    private ArrayList<PermissionType> lstPermissionType;
    private DatabaseAdapter databaseAdapter;
    private TelephonyManager telephonyManager;

    /*
    =======================================
    ACTIVITY LIFECYCLE METHODS
    =======================================
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        lstPermissionType = new ArrayList<PermissionType>();
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (ImageButton) findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        if (getLoginUserStatus().equals(Constants.LOGGED_USER)) {
            this.user = databaseAdapter.getUser();
            this.device = databaseAdapter.getDevice();
            this.user.setCompany(databaseAdapter.getCompany());
            this.lstPermissionType = databaseAdapter.getPermissionType();
            navidateToDashboard();
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 225);
            }
        }

    }

    /*
    =======================================
    BUSINESS LOGIC METHODS
    =======================================
     */

    //Layout GUI methods
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                email = txtEmail.getText().toString().replaceAll("\\s", "");
                password = txtPassword.getText().toString().replaceAll("\\s", "");
                if ((TextUtils.isEmpty(email) || (TextUtils.isEmpty(password)))) {
                    txtEmail.setError("Parámetro requerido");
                    txtPassword.setError("Parámetro requerido");
                } else {
                    if (checkSelfPermission(permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        UserCredential userCredential = new UserCredential(email, password);
                        showLoginProgressDialog("Autenticando usuario");
                        login(userCredential);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
                    }
                }
                break;
        }

    }

    //Login user
    public void login(final UserCredential userCredential) {
        LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);
        Call<JsonObject> call = loginRetrofitInterface.login(userCredential);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
                        } else {
                            if (android.os.Build.VERSION.SDK_INT >= 23 && android.os.Build.VERSION.SDK_INT < 26) {
                                deviceImei = telephonyManager.getDeviceId();
                            }
                            if (android.os.Build.VERSION.SDK_INT >= 26) {
                                deviceImei = telephonyManager.getImei();
                            }
                        }
                    }
                    user = new User();
                    Company company = new Company();
                    ArrayList<WorkingPeriod> workingPeriodList = new ArrayList<WorkingPeriod>();
                    user.setId(response.body().getAsJsonObject("data").get("id").getAsInt());
                    user.setNombres(response.body().getAsJsonObject("data").get("nombres").getAsString());
                    user.setApellidos(response.body().getAsJsonObject("data").get("apellidos").getAsString());
                    user.setEmail(response.body().getAsJsonObject("data").get("email").getAsString());
                    company.setName(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("nombre").getAsString());
                    company.setLatitude(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("latitud").getAsDouble());
                    company.setLongitude(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("longitud").getAsDouble());
                    company.setRadius(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("radio").getAsFloat());
                    user.setCompany(company);
                    setUserToken(response.body().get("token").toString().replace("\"", ""));
                    databaseAdapter.insertUser(user);
                    databaseAdapter.insertCompany(company);
                    changeWorkingState(Constants.STATE_NOT_WORKING);
                    setLoggedUser();
                    findUserDevice();
                } else if (response.code() == 404 || response.code() == 401) {
                    hideLoginProgressDialog();
                    showCredentialsErrorMessage();
                } else if (response.code() == 502) {
                    hideLoginProgressDialog();
                } else {
                    hideLoginProgressDialog();
                    try {
                        JSONObject errorJson = new JSONObject(response.errorBody().string());
                        showErrorMessage(errorJson.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showConectionErrorMessage();
            }

        });
    }

    //Get user device
    public void findUserDevice() {
        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> deviceCall = deviceRetrofitInterface.get(getUserToken(), user.getId());
        deviceCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("deviceImei", "DEVICE ON RESPONSE OK");

                JsonArray deviceList = response.body().getAsJsonArray("data");
                boolean deviceFound = false;
                for (int i = 0; i < deviceList.size() && deviceFound == false; i++) {
                    JsonObject jsonDevice = deviceList.get(i).getAsJsonObject();

                    if (deviceImei.equals(jsonDevice.get("imei").getAsString())) {
                        device = new Device();
                        int deviceId = jsonDevice.get("id").getAsInt();
                        String deviceName = jsonDevice.get("nombre").getAsString();
                        String deviceModel = jsonDevice.get("modelo").getAsString();
                        String deviceImei = jsonDevice.get("imei").getAsString();
                        boolean deviceStatus = jsonDevice.get("estado").getAsBoolean();
                        device.setId(deviceId);
                        device.setNombre(deviceName);
                        device.setModelo(deviceModel);
                        device.setImei(deviceImei);
                        device.setStatus(deviceStatus);
                        databaseAdapter.insertDevice(device);
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
        final Call<JsonObject> permissionTypeCall = permissionTypeRetrofitInterface.get(getUserToken());
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
                        databaseAdapter.insertPermissionType(permissionType);
                        lstPermissionType.add(permissionType);
                    }
                }
                hideLoginProgressDialog();
                navidateToDashboard();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    //Change to BottomNavigation activity
    public void navidateToDashboard() {
        Intent intent = new Intent(this, BottomNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        if (device != null) {
            bundle.putSerializable("device", device);
        }
        bundle.putSerializable("user", user);
        bundle.putSerializable("lstPermissionType", lstPermissionType);
        intent.putExtras(bundle);
        Log.d("DeviceLog", "navigation activated!");
        startActivity(intent);
        finish();
    }

    //Shared preferences methods
    public void setUserToken(String token) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_TOKEN, token);
        editor.commit();
    }

    public String getUserToken() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.USER_TOKEN,
                "");
    }

    private void setLoggedUser() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.LOGIN_USER_STATE, Constants.LOGGED_USER);
        editor.commit();
    }

    private String getLoginUserStatus() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.LOGIN_USER_STATE,
                "");
    }

    private void changeWorkingState(String state) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.CURRENT_STATE, state);
        editor.commit();
    }

    //Dialogs
    public void showLoginProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void hideLoginProgressDialog() {
        progressDialog.dismiss();
    }

    public void showErrorMessage(String error) {
        showDialog("Error", error);
    }

    public void showCredentialsErrorMessage() {
        showDialog("Autenticación fallida", "El usuario y contraseña proporcionados no son correctos.");
    }

    public void showConectionErrorMessage() {
        showDialog("Error de conexión", "Al parecer no hay conexión a Internet.");
    }

    public void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
