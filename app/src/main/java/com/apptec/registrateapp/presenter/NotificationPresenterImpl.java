package com.apptec.registrateapp.presenter;

import com.apptec.registrateapp.interactor.NotificationInteractorImpl;
import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.view.fragments.notifications.NotificationView;

import java.util.ArrayList;

public class NotificationPresenterImpl implements NotificationPresenter{
    /*
    * This class is in the middle of the MVP pattern.
    * It interacts as an intermediary between the Model and the View.
    * */

    // Instance of Interactor and View
    private NotificationView notificationView;
    private NotificationInteractorImpl notificationInteractor;

    public NotificationPresenterImpl(NotificationView notificationView) {
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
        if(notificationView!=null) {
            notificationView.showNotifications(notifications);
        }
    }

    @Override
    public void showNotNewNotificationsMessage() {
        /**
         * Calling the view
         */
        if(notificationView!=null) {
        notificationView.showNotNewNotificationsMessage();}
    }

    @Override
    public void detachView() {
        if(notificationView!=null) {
        notificationView = null;}
    }

    @Override
    public void detachJob() {
        notificationInteractor.detachJob();
        notificationInteractor = null;
    }

    @Override
    public void loadNotifications() {
        notificationInteractor.loadNotifications();
    }
}
