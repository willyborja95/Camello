package com.apptec.registrateapp.loginactivity;

public interface LoginActivityView {
    /**
    * The interface for the view.
    * */

    // ToDo: Pass the LoginActivity methods here
    void navigateToNextView();

    void showMessage(String title, String message);
    void showAlertDialog(String title, String message);

    void showLoginProgressDialog(String message);
    void hideLoginProgressDialog();

}
