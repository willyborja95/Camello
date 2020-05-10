package com.apptec.registrateapp.mainactivity.fnotification.ui;

public interface NotificationView {
    /*
     * Notification View interface
     * */


    void showNotNewNotificationsMessage();

    void showAssistanceProgressDialog(String string);

    void hideAssistanceProgressDialog();

    void showConnectionErrorMessage();

    void showDialog(String title, String message);
}
