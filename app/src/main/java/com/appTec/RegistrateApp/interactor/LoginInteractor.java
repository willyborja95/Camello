package com.appTec.RegistrateApp.interactor;

public interface LoginInteractor {


    void getInitialData();  // From the presenter
    void loadInitialData(); // To the presenter
    void handleLogin(String email, String password);     // Call the webservice


}
