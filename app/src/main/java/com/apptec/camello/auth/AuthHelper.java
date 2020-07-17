package com.apptec.camello.auth;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.apptec.camello.App;
import com.apptec.camello.util.Constants;

import timber.log.Timber;

public class AuthHelper {
    /**
     * This class will contain auth stuff
     * <p>
     * - This class will login and logout the user
     * - Manage for device permissions stuff
     */

    // Work manager
    private static WorkManager workManager = WorkManager.getInstance(App.getContext());




    /**
     *
     */
    public static void scheduleSync() {
        Timber.d("Scheduling sync when internet is available");
        // Constraints: Do the work if the the network is connected
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a work request
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(
                SyncAssistanceWorker.class)
                .setConstraints(constraints)
                .build();

        workManager.enqueueUniqueWork(
                Constants.SYNC_JOB_ID + "",
                ExistingWorkPolicy.APPEND,
                syncRequest
        );

    }







}
