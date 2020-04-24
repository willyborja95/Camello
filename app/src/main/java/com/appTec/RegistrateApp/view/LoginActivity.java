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
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.appTec.RegistrateApp.MainActivity;
import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.models.UserCredential;
import com.appTec.RegistrateApp.models.WorkingPeriod;
import com.appTec.RegistrateApp.presenter.LoginPresenterImpl;
import com.appTec.RegistrateApp.repository.StaticData;
import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.DeviceRetrofitInterface;
import com.appTec.RegistrateApp.repository.webServices.interfaces.LoginRetrofitInterface;
import com.appTec.RegistrateApp.repository.webServices.interfaces.PermissionTypeRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.Manifest.permission;


import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener, LoginActivityView {

    // UI elements
    private ImageButton btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;
    ProgressDialog progressDialog;

    // Business logic elements
    private TelephonyManager telephonyManager;



    // Instance the presenter
    LoginPresenterImpl loginPresenter = new LoginPresenterImpl(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);                                                  // Showing the splash screen for until the activity is ready
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);                                        // Binding the layout

        // Binding UI elements
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (ImageButton) findViewById(R.id.loginButton);
        progressDialog = new ProgressDialog(this);



        btnLogin.setOnClickListener(this);


        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        // Checking if it is the first running
        if (loginPresenter.isTheFirstRun()){
            loginPresenter.handleFirstRun(this);
        }


        // Verified if a user is still saved
        loginPresenter.verifyPreviousLogin();


    }


    //Layout GUI methods
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                String email = txtEmail.getText().toString().replaceAll("\\s", "");
                String password = txtPassword.getText().toString().replaceAll("\\s", "");
                if ((TextUtils.isEmpty(email) || (TextUtils.isEmpty(password)))) {
                    txtEmail.setError(getString(R.string.parameter_missing_error));
                    txtPassword.setError(getString(R.string.parameter_missing_error));
                } else {
                    if (checkSelfPermission(permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        UserCredential userCredential = new UserCredential(email, password);
                        showLoginProgressDialog(getString(R.string.login_progress_dialog_message));
                        loginPresenter.handleLogin(userCredential);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
                    }
                }
                break;
        }

    }



    //Change to BottomNavigation activity
    public void navigateToNextView() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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


    @Override
    public void showMessage(String title, String message) {
        showAlertDialog(title, message);
    }



    @Override
    public void showAlertDialog(String title, String message) {
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
