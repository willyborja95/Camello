package com.appTec.RegistrateApp.view.fragments.notifications;

import com.appTec.RegistrateApp.models.Notification;

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
