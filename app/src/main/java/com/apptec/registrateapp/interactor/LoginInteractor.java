package com.apptec.registrateapp.interactor;

import android.app.Activity;

import com.apptec.registrateapp.models.UserCredential;

public interface LoginInteractor {



    void verifyPreviousLogin(); // From the presenter
    void handleLogin(UserCredential userCredential);    // From the presenter
    void handleFirstRun(Activity activity); // When the app is running by first time or is reinstalled
    boolean isTheFirstRun();
}
