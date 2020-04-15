package com.appTec.RegistrateApp.presenter;

import android.app.Activity;
import android.content.Context;

public interface LoginPresenter {
    /**
     * Interface for login presenter
     */

    void getInitialData(Activity activity);  // From the view
    void loadInitialData(); // To the interactor
    void verifyPreviousLogin(Context context); // To the interactor
    void navigateToNextView();  // Calling the view
    void handleLogin(String email, String password);


}
