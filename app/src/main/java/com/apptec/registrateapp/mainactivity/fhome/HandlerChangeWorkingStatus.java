package com.apptec.registrateapp.mainactivity.fhome;

import android.util.Log;

import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.util.Constants;

public class HandlerChangeWorkingStatus implements Runnable {
    /**
     * This class will is a Runnable that handles the working status change
     */

    private final String TAG = HandlerChangeWorkingStatus.class.getSimpleName();

    @Override
    public void run() {

        /**
         * Change the working status
         */
        Log.d(TAG, "Init change working status");


        if (RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod() == null) {
            /** We entry here when is the first time when the button is pressed since the app was installed.*/
            Log.d(TAG, "Not worker initialized");
            Log.i(TAG, "New worker created");

            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);

        } else if (isWorking()) {
            /** If the user is working now, then finish whe work and create a new one */
            Log.i(TAG, "Finishing job and creating a new one");
            RoomHelper.getAppDatabaseInstance().workingPeriodDao().changeLastWorkingPeriod(Constants.INT_FINISHED_STATUS);
            notifyTheServer();
            createAndSaveWorkingPeriod(Constants.INT_NOT_INIT_STATUS);
        } else if (isNotInitWorking()) {
            /** If the period of working is not init start it*/
            Log.i(TAG, "A new working period created");
            notifyTheServer();
            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);

        }

    }


    private void notifyTheServer() {
        // TODO: Notify that the working status has change.
        //  Send the work zone id.

    }

    private boolean isWorking() {
        /**
         * Return true when the last working status was 'working'
         */
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_WORKING_STATUS;
    }

    private boolean isNotInitWorking() {
        /**
         * Return true when the last working status was 'not init'
         */
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_NOT_INIT_STATUS;
    }

    private void createAndSaveWorkingPeriod(int status) {
        /**
         * Create a new working period with
         * the param
         * @param status For example could be: 'not init'
         */
        WorkingPeriodModel startedWorkingPeriod = new WorkingPeriodModel();
        startedWorkingPeriod.setStatus(status);
        RoomHelper.getAppDatabaseInstance().workingPeriodDao().insert(startedWorkingPeriod);
    }


}
