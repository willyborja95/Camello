package com.appTec.RegistrateApp.presenter;

import android.app.Activity;
import android.content.Context;

import com.appTec.RegistrateApp.interactor.LoginInteractorImpl;
import com.appTec.RegistrateApp.view.LoginActivityView;

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
    public void getInitialData(Activity activity) {
        /**
         * Call the interactor
         */
        loginInteractor.getInitialData(activity);
    }

    @Override
    public void loadInitialData() {
        /**
         * Call the view
         */
        // loginActivityView.loadInitialData();
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
    public void handleLogin(String email, String password) {
        /**
         * Calling the interactor
         */
        loginInteractor.handleLogin(email, password);
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
}
