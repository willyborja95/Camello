package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications;

import com.appTec.RegistrateApp.models.Notification;

import java.util.ArrayList;

public interface NotificationView {
    /*
    * Notification View interface
    * */


    void getNotifications(); // Presenter
    void showNotifications(ArrayList<Notification> notifications);


}
