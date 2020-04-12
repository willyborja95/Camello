package com.appTec.RegistrateApp.interactor;

import com.appTec.RegistrateApp.models.Notification;

import java.util.ArrayList;

public interface NotificationInteractor {


    void getNotifications(); // Get the notifications from web service
    void returnNotifications(ArrayList<Notification> notifications); // To presenter

}
