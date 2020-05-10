package com.apptec.registrateapp.mainactivity.fnotification;

public class NotificationInteractorImpl implements NotificationInteractor {
    /*
     * This class will get for the presenter all the data requested
     * */

    // Attributes
    private NotificationPresenterImpl notificationPresenter;
    private boolean canceled;

    public NotificationInteractorImpl(NotificationPresenterImpl notificationPresenter) {
        /*
         * Constructor
         * */

        this.notificationPresenter = notificationPresenter;
    }


    @Override
    public void detachJob() {
        canceled = true;
        notificationPresenter = null;
    }



}
