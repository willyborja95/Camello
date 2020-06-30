package com.apptec.registrateapp.mainactivity.fhome;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.util.Constants;

import timber.log.Timber;

/**
 * This class will is a Runnable that handles the working status change
 */
public class HandlerChangeWorkingStatus implements Runnable {

    // This instance of WorkZoneModel will be useful when the transition will be an enter
    private WorkZoneModel workZoneModel;

    /**
     * Constructor called when the transition will be an enter
     *
     * @param workZoneModel
     */
    public HandlerChangeWorkingStatus(WorkZoneModel workZoneModel) {
        this.workZoneModel = workZoneModel;
    }

    /**
     * Empty constructor when we don't know which is the work zone
     */
    public HandlerChangeWorkingStatus() {
    }

    @Override
    public void run() {

        /**
         * Change the working status
         */
        Timber.d("Init change working status");


        if (RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod() == null) {
            /** We entry here when is the first time when the button is pressed since the app was installed.*/
            Timber.d("Not worker initialized");
            Timber.i("New worker created");

            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);
            App.getGeofenceHelper().startTracking(workZoneModel);
            notifyTheServer();
        } else if (isWorking()) {
            /** If the user is working now, then finish whe work and create a new one */
            Timber.i("Finishing job and creating a new one");
            RoomHelper.getAppDatabaseInstance().workingPeriodDao().changeLastWorkingPeriod(Constants.INT_FINISHED_STATUS);
            createAndSaveWorkingPeriod(Constants.INT_NOT_INIT_STATUS);
            App.getGeofenceHelper().stopTracking();
            notifyTheServer();
        } else if (isNotInitWorking()) {
            /** If the period of working is not init start it*/
            Timber.i("A new working period created");
            notifyTheServer();
            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);
            App.getGeofenceHelper().startTracking(workZoneModel);
            notifyTheServer();
        }

    }


    /**
     * TODO: Notify that the working status has change.
     * Send the work zone id.
     */
    private void notifyTheServer() {


    }

    /**
     * Return true when the last working status was 'working'
     *
     * @return
     */
    private boolean isWorking() {

        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_WORKING_STATUS;
    }

    /**
     * Return true when the last working status was 'not init'
     *
     * @return
     */
    private boolean isNotInitWorking() {
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_NOT_INIT_STATUS;
    }

    /**
     * Create a new working period with
     * the param
     *
     * @param status For example could be: 'not init'
     */
    private void createAndSaveWorkingPeriod(int status) {
        WorkingPeriodModel startedWorkingPeriod = new WorkingPeriodModel();
        startedWorkingPeriod.setStatus(status);
        RoomHelper.getAppDatabaseInstance().workingPeriodDao().insert(startedWorkingPeriod);
    }


}
