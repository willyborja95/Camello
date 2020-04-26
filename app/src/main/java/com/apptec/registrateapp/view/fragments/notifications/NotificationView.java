package com.apptec.registrateapp.view.fragments.notifications;

import com.apptec.registrateapp.models.Notification;

import java.util.ArrayList;

public interface NotificationView {
    /*
    * Notification View interface
    * */



    void showNotifications(ArrayList<Notification> notifications);
    void showNotNewNotificationsMessage();

    void showAssistanceProgressDialog(String string);
    void hideAssistanceProgressDialog();
    void showConnectionErrorMessage();
    void showDialog(String title, String message);
}
