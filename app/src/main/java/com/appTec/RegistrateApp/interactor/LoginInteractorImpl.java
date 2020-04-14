package com.appTec.RegistrateApp.interactor;

import com.appTec.RegistrateApp.presenter.LoginPresenterImpl;

public class LoginInteractorImpl implements LoginInteractor {
    /**
     * Implementation of the interface
     */

    // Attributes
    LoginPresenterImpl loginPresenter; // Got as an attribute


    // Constructor
    public LoginInteractorImpl(LoginPresenterImpl loginPresenter) {
        /**
         * Constructor
         */
        this.loginPresenter = loginPresenter;
    }


    @Override
    public void getInitialData() {
        /**
         * Get the data needed
         */
        // ToDo: Get tha data needed to the login case
    }

    @Override
    public void loadInitialData() {
        /**
         * Call the presenter
         */
        loginPresenter.loadInitialData();

    }

    @Override
    public void verifyPreviousLogin() {
        /**
         * Verifying if credentials are saved
         */

    }
}

