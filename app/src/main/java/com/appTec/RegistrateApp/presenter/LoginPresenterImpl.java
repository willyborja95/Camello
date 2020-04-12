package com.appTec.RegistrateApp.presenter;

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
    public void getInitialData() {
        /**
         * Call the interactor
         */
        loginInteractor.getInitialData();
    }

    @Override
    public void loadInitialData() {
        /**
         * Call the view
         */
        loginActivityView.loadInitialData();
    }
}
