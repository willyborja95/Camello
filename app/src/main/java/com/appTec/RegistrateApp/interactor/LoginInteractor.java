package com.appTec.RegistrateApp.interactor;

import android.app.Activity;
import android.content.Context;

import com.appTec.RegistrateApp.models.UserCredential;

public interface LoginInteractor {



    void verifyPreviousLogin(); // From the presenter
    void handleLogin(UserCredential userCredential);    // From the presenter
    void handleFirstRun(Activity activity); // When the app is running by first time or is reinstalled
    boolean isTheFirstRun();
}
