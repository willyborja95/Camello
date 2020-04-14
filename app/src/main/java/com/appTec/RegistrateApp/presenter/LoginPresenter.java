package com.appTec.RegistrateApp.presenter;

public interface LoginPresenter {
    /**
     * Interface for login presenter
     */

    void getInitialData();  // From the view
    void loadInitialData(); // To the interactor
    void verifyPreviousLogin(); // To the interactor



}
