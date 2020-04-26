package com.apptec.registrateapp.presenter;

import android.app.Activity;

import com.apptec.registrateapp.models.UserCredential;

public interface LoginPresenter {
    /**
     * Interface for login presenter
     */



    void verifyPreviousLogin(); // To the interactor
    void navigateToNextView();  // To the view
    void handleLogin(UserCredential userCredential); // To the interactor
    void handleFirstRun(Activity activity); // When is the first time tha the app run on the device.
    boolean isTheFirstRun();

    void showLoginProgressDialog(String message);
    void hideLoginProgressDialog();

    void showMessage(String title, String message);
    void showAlertDialog(String title, String message);

}
