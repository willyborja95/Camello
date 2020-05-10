package com.apptec.registrateapp.mainactivity.fnotification;

public class NotificationPresenterImpl implements NotificationPresenter {
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


}
