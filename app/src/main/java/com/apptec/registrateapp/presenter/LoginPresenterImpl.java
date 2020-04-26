package com.apptec.registrateapp.presenter;

import android.app.Activity;

import com.apptec.registrateapp.interactor.LoginInteractorImpl;
import com.apptec.registrateapp.models.UserCredential;
import com.apptec.registrateapp.LoginActivityView;

public class LoginPresenterImpl implements LoginPresenter {

    // Attributes
    LoginActivityView loginActivityView; // Passed as attribute
    LoginInteractorImpl loginInteractor; // Instanced here


    // Constructor
    public LoginPresenterImpl(LoginActivityView loginActivityView) {
        /**
         * The presenter will be the intermediary
         *
         */
        this.loginActivityView = loginActivityView;
        this.loginInteractor = new LoginInteractorImpl(this);

    }


    @Override
    public void verifyPreviousLogin() {
        /**
         * Call the interactor
         */
        loginInteractor.verifyPreviousLogin();

    }

    @Override
    public void navigateToNextView() {
        /**
         * Call the view
         */
        loginActivityView.navigateToNextView();
    }

    @Override
    public void handleLogin(UserCredential userCredential) {
        /**
         * Calling the interactor
         */
        loginInteractor.handleLogin(userCredential);
    }

    @Override
    public void handleFirstRun(Activity activity) {
        /**
         * When the app is running by first time
         */
        loginInteractor.handleFirstRun(activity);
    }

    @Override
    public boolean isTheFirstRun() {
        return loginInteractor.isTheFirstRun();
    }

    @Override
    public void showLoginProgressDialog(String message) {
        loginActivityView.showLoginProgressDialog(message);
    }

    @Override
    public void hideLoginProgressDialog() {
        loginActivityView.hideLoginProgressDialog();
    }

    @Override
    public void showMessage(String title, String message) {
        loginActivityView.showMessage(title, message);
    }

    @Override
    public void showAlertDialog(String title, String message) {
        loginActivityView.showAlertDialog(title, message);
    }
}
