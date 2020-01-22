package com.appTec.RegistrateApp.view;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.Manifest.permission;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener {

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


        if (databaseAdapter.getLoginStatus() == 1) {
            this.device = databaseAdapter.getDevice();
            this.lstPermissionType = databaseAdapter.getPermissionType();
            navidateToDashboard();

        }

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            System.out.println("ESTE ES MI ID ***************************************************");
            if (checkSelfPermission(permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
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
                    UserCredential userCredential = new UserCredential(email, password);
                    showProgress();
                    login(userCredential);
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
                    if (android.os.Build.VERSION.SDK_INT >= 26) {
                        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
                        } else {
                            deviceImei = telephonyManager.getImei();
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
                    company.setRadius(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("radio").getAsDouble());
                    user.setCompany(company);
                    String token = response.body().get("token").toString().replace("\"", "");
                    editor.putString("token", token);
                    editor.commit();
                    databaseAdapter.insertUser(user);
                    databaseAdapter.insertLoginStatus(1);

                    DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
                    Call<JsonObject> deviceCall = deviceRetrofitInterface.get(token, user.getId());
                    deviceCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonArray deviceList = response.body().getAsJsonArray("data");
                            Device device = null;
                            for (int i = 0; i < deviceList.size(); i++) {
                                JsonObject jsonDevice = deviceList.get(i).getAsJsonObject();
                                if (deviceImei.equals(jsonDevice.get("imei").getAsString()) && jsonDevice.get("estado").getAsBoolean() == true) {
                                    int deviceId = jsonDevice.get("id").getAsInt();
                                    String deviceName = jsonDevice.get("nombre").getAsString();
                                    String deviceModel = jsonDevice.get("modelo").getAsString();
                                    String deviceImei = jsonDevice.get("imei").getAsString();
                                    boolean deviceStatus = jsonDevice.get("estado").getAsBoolean();
                                    device = new Device(deviceId, deviceName, deviceModel, deviceImei, deviceStatus);
                                    databaseAdapter.insertDevice(device);
                                }
                            }
                            //******************************************************************
                            //TOMAR EN CUENTA
                            setDevice(device);
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
                    System.out.println("USUARIO NO ENCONTRADO ==================================================");
                    showCredentialsErrorMessage();
                }

                System.out.println(response.toString());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("NO TENEMOS RESPUESTA");
                System.out.println(call.toString());
                System.out.println(t.toString());
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
        System.out.println("error de conexion -------------------------------------------------------------");
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

    private void setDevice(Device device){
        this.device = device;
    }

    public void navidateToDashboard() {
        Intent intent = new Intent(this, BottomNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();

        bundle.putSerializable("device", (Device) device);
        bundle.putSerializable("user", (User) user);
        bundle.putSerializable("lstPermissionType", lstPermissionType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
