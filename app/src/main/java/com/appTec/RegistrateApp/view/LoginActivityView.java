package com.appTec.RegistrateApp.view;

public interface LoginActivityView {
    /**
    * The interface for the view.
    * */

    // ToDo: Pass the LoginActivity methods here

    void getInitialData(); // To the presenter
    void loadInitialData(); // From the presenter // ToDo: Add the necessary attributes
    void handleLogin(String email, String password); // To the presenter



}
