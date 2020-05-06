package com.apptec.registrateapp;

import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.apptec.registrateapp.models.UserCredential;
import com.apptec.registrateapp.presenter.LoginPresenterImpl;

public class LoginActivity extends Activity implements View.OnClickListener, LoginActivityView {
    /**
     * This is the launcher activity
     */

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

        // This instance help us to get the permissions to hardware
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        // Checking if it is the first running
        if (loginPresenter.isTheFirstRun()) {
            loginPresenter.handleFirstRun(this);
        }


        // Verified if a user is still saved
        loginPresenter.verifyPreviousLogin();


    }


    //Layout GUI methods
    @Override
    public void onClick(View v) {
        /**
         * Here handle the click on the login button
         */
        switch (v.getId()) {
            case R.id.loginButton:
                String email = txtEmail.getText().toString().replaceAll("\\s", "");
                String password = txtPassword.getText().toString().replaceAll("\\s", "");
                if ((TextUtils.isEmpty(email) || (TextUtils.isEmpty(password)))) { // Validation
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




    public void navigateToNextView() {
        /**
         * Navigate to the next activity
         */
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void showLoginProgressDialog(String message) {
        /** Dialog */
        progressDialog.setMessage(message);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void hideLoginProgressDialog() {
        /** Hide the dialog from th screen */
        progressDialog.dismiss();
    }


    @Override
    public void showMessage(String title, String message) {
        /** Shows a message on the screen */
        showAlertDialog(title, message);
    }



    @Override
    public void showAlertDialog(String title, String message) {
        /** Show an alert dialog */
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
