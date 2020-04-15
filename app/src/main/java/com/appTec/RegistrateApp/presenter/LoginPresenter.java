package com.appTec.RegistrateApp.presenter;

import android.app.Activity;
import android.content.Context;

public interface LoginPresenter {
    /**
     * Interface for login presenter
     */



    void verifyPreviousLogin(); // To the interactor
    void navigateToNextView();  // To the view
    void handleLogin(String email, String password); // To the interactor
    void handleFirstRun(Activity activity); // When is the first time tha the app run on the device.
    boolean isTheFirstRun();

}
