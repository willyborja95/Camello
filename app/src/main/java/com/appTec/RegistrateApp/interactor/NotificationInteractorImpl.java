package com.appTec.RegistrateApp.interactor;

import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.presenter.NotificationPresenter;
import com.appTec.RegistrateApp.presenter.NotificationPresenterImpl;

import java.util.ArrayList;

public class NotificationInteractorImpl implements NotificationInteractor {
    /*
    * This class will get for the presenter all the data requested
    * */

    // Attributes
    private NotificationPresenterImpl notificationPresenter;

    public NotificationInteractorImpl(NotificationPresenterImpl notificationPresenter) {
        /*
        * Constructor
        * */
        this.notificationPresenter = notificationPresenter;
    }

    @Override
    public void getNotifications() {

    }

    @Override
    public void returnNotifications(ArrayList<Notification> notifications) {

    }
}
