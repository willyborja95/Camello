package com.appTec.RegistrateApp.presenter;

import android.view.View;

import com.appTec.RegistrateApp.models.Notification;

import java.util.ArrayList;

public interface NotificationPresenter {

    void getNotifications(); // To interactor
    void showNotifications(ArrayList<Notification> notifications); // To view


}
