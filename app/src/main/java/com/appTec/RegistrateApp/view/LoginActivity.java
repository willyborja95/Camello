package com.appTec.RegistrateApp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.appTec.RegistrateApp.BuildConfig;
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
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.Manifest.permission;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    //UI elements
    private ProgressBar progressBar;
    private ImageButton btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;

    //App elements
    private String email;
    private String password;
    private String deviceImei;
    private Device device;
    private User user;
    private ArrayList<PermissionType> lstPermissionType;
    DatabaseAdapter databaseAdapter;
    TelephonyManager telephonyManager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        lstPermissionType = new ArrayList<PermissionType>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (ImageButton) findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(this);
        hideProgress();
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this);
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        pref = getApplicationContext().getSharedPreferences("RegistrateApp", 0); // 0 - for private mode
        editor = pref.edit();

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
                        showProgress();
                        login(userCredential);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
                    }
                }
                break;
        }

    }

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
                    user.setNombres(response.body().getAsJsonObject("data").get("nombres").toString());
                    user.setApellidos(response.body().getAsJsonObject("data").get("apellidos").toString());
                    user.setEmail(response.body().getAsJsonObject("data").get("email").toString());
                    company.setName(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("nombre").toString());
                    company.setLatitude(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("latitud").getAsDouble());
                    company.setLongitude(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("longitud").getAsDouble());
                    company.setRadius(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("radio").getAsFloat());
                    user.setCompany(company);
                    String token = response.body().get("token").toString().replace("\"", "");
                    editor.putString("token", token);
                    editor.commit();
                    databaseAdapter.insertUser(user);
                    databaseAdapter.insertCompany(company);
                    setLoggedUser();

                    DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
                    Call<JsonObject> deviceCall = deviceRetrofitInterface.get(token, user.getId());
                    deviceCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonArray deviceList = response.body().getAsJsonArray("data");
                            for (int i = 0; i < deviceList.size(); i++) {
                                JsonObject jsonDevice = deviceList.get(i).getAsJsonObject();
                                if (deviceImei.equals(jsonDevice.get("imei").getAsString())) {
                                    int deviceId = jsonDevice.get("id").getAsInt();
                                    String deviceName = jsonDevice.get("nombre").getAsString();
                                    String deviceModel = jsonDevice.get("modelo").getAsString();
                                    String deviceImei = jsonDevice.get("imei").getAsString();
                                    boolean deviceStatus = jsonDevice.get("estado").getAsBoolean();
                                    device = new Device(deviceId, deviceName, deviceModel, deviceImei, deviceStatus);
                                    databaseAdapter.insertDevice(device);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });


                    //Get PermissionType
                    PermissionTypeRetrofitInterface permissionTypeRetrofitInterface = ApiClient.getClient().create(PermissionTypeRetrofitInterface.class);
                    final Call<JsonObject> permissionTypeCall = permissionTypeRetrofitInterface.get(token);
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
                            navidateToDashboard();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });


                } else if (response.code() == 404 || response.code() == 401) {
                    showCredentialsErrorMessage();
                }

                System.out.println(response.toString());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showConectionErrorMessage();
            }

        });
    }

    public void showProgress() {
        btnLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void showCredentialsErrorMessage() {
        InformationDialog.createDialog(this);
        InformationDialog.setTitle("Autenticación fallida");
        InformationDialog.setMessage("El usuario y contraseña proporcionados no son correctos.");
        InformationDialog.showDialog();
        hideProgress();
    }

    public void showConectionErrorMessage() {
        InformationDialog.createDialog(this);
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
        hideProgress();
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
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
        startActivity(intent);
    }

}
