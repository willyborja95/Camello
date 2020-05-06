package com.apptec.registrateapp.repository.workers.WorkingPeriod;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apptec.registrateapp.models.WorkingPeriod;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.util.Constants;

public class ChangeWorkingStatus extends Worker {

    private final String TAG = "ChangeWorkingStatus";

    public ChangeWorkingStatus(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        /**
         * Change the working status
         */
        Log.d(TAG, "Init change working status");
        // TODO:
        Log.d(TAG, "Working period status: " + RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus());
        Log.d(TAG, "Working period id: " + RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getId());
        if (RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod() == null) {
            Log.d(TAG, "Not worker initialized");
            Log.i(TAG, "New worker created");

            createAndSaveWorkingPeriod(Constants.INT_NOT_INIT_STATUS);

        } else if (isWorking()) {
            Log.i(TAG, "Finishing job and creating anew one");
            RoomHelper.getAppDatabaseInstance().workingPeriodDao().changeLastWorkingPeriod(Constants.INT_FINISHED_STATUS);
            notifyTheServer();
            createAndSaveWorkingPeriod(Constants.INT_NOT_INIT_STATUS);
        } else if (isNotInitWorking()) {
            Log.i(TAG, "A new working period created");
            notifyTheServer();
            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);

        }
        return Result.success();

    }

    private void notifyTheServer() {
        // TODO: Notify that the working status has change. Send the work zone id.

    }

    private boolean isWorking() {
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_WORKING_STATUS;
    }

    private boolean isNotInitWorking() {
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_NOT_INIT_STATUS;
    }

    private void createAndSaveWorkingPeriod(int status) {
        WorkingPeriod startedWorkingPeriod = new WorkingPeriod();
        startedWorkingPeriod.setStatus(status);
        RoomHelper.getAppDatabaseInstance().workingPeriodDao().insert(startedWorkingPeriod);
    }

    ;


}
