package com.apptec.camello.auth;

import androidx.annotation.Nullable;
import androidx.work.WorkManager;

import com.apptec.camello.App;
import com.apptec.camello.mainactivity.fhome.geofence.StopWorking;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;

import timber.log.Timber;

/**
 * This class that implements a runnable will be the only way to logout the user for any part
 * of the app
 */
public class SignOutRunnable implements Runnable {


    // Variables of control
    private boolean shouldStopSyncWorker;
    private boolean shouldStopWorking;


    // listener instance
    private SignOutListener listener;


    /**
     * Constructor of this runnable
     *
     * @param shouldStopSyncWorker a boolean to know if we should stop the {@link SyncAssistanceWorker}
     * @param listener             listener of this process
     */
    public SignOutRunnable(boolean shouldStopSyncWorker, boolean shouldStopWorking, @Nullable SignOutListener listener) {
        this.shouldStopSyncWorker = shouldStopSyncWorker;
        this.shouldStopWorking = shouldStopWorking;
        this.listener = listener;
    }

    /**
     * Interface of the listener of this runnable
     */
    public static interface SignOutListener {

        /**
         * Method called when the process finished.
         * Should start the login activity if you are on the UI thread.
         */
        void onSuccessFinished();
    }


    /**
     * Here we are going to close some background threads and more:
     * - stop the refresh token worker
     * - stopSyncWorker is true, stop the {@link SyncAssistanceWorker} using the {@link androidx.work.WorkManager}
     * -
     */
    @Override
    public void run() {


        if (shouldStopWorking) {
            new Thread(new StopWorking(null)).start();
        }


        if (shouldStopSyncWorker) {
            stopSyncWorker();
        }


        stopRefreshTokenWorker();

        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, "");
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, "");
        SharedPreferencesHelper.putBooleanValue(Constants.IS_USER_LOGGED, false);

        RoomHelper.getAppDatabaseInstance().deviceDao().deleteAll();

        // Notify the listener, and he should will start the login activity
        if (listener != null) {
            listener.onSuccessFinished();
        }


    }


    /**
     * Method to take out the worker for sync the last assistance from the WorkManager's queue
     */
    private void stopSyncWorker() {
        // ? TODO
    }


    /**
     * Method to take out the worker for refresh token from the WorkManager's queue
     */
    private void stopRefreshTokenWorker() {
        try {
            WorkManager.getInstance(App.getContext()).cancelUniqueWork(Constants.WORKER_REFRESHER);
        } catch (Exception e) {
            Timber.d(e, "Failed when trying to stop refresh worker");
        }
    }
}
