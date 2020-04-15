package com.appTec.RegistrateApp.interactor;

import android.app.Activity;
import android.content.Context;

public interface LoginInteractor {


    void getInitialData(Activity activity);  // From the presenter
    void loadInitialData(); // To the presenter
    void verifyPreviousLogin(); // From the presenter
    void handleLogin(String email, String password);    // From the presenter

}
