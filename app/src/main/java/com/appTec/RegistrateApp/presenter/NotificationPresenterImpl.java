package com.appTec.RegistrateApp.presenter;

import android.view.View;

import com.appTec.RegistrateApp.interactor.NotificationInteractor;
import com.appTec.RegistrateApp.interactor.NotificationInteractorImpl;
import com.appTec.RegistrateApp.models.Notification;

import java.util.ArrayList;

public class NotificationPresenterImpl implements NotificationPresenter{
    /*
    * This class is in the middle of the MVP pattern.
    * It interacts as an intermediary between the Model and the View.
    * */

    // Instance of Interactor and View
    private View notificationView;
    private NotificationInteractorImpl notificationInteractor;

    public NotificationPresenterImpl(View notificationView) {
        /*
        * Constructor
        * */
        this.notificationView = notificationView;
        this.notificationInteractor = new NotificationInteractorImpl(this);
    }

    @Override
    public void getNotifications(){
        /*
        * Calling the interactor
        * */
        notificationInteractor.getNotifications();
    }

    @Override
    public void showNotifications(ArrayList<Notification> notifications) {
        /*
        * Calling the view
        * */
        // notificationView.showNotifications();
    }
}
