package com.appTec.RegistrateApp.presenter;

import android.content.Context;

import com.appTec.RegistrateApp.models.Notification;

import java.util.ArrayList;

public interface NotificationPresenter {

    void getNotifications(); // To interactor
    void showNotifications(ArrayList<Notification> notifications); // To view
    void showNotNewNotificationsMessage();



    void detachView();
    void detachJob();


    void loadNotifications();
}
