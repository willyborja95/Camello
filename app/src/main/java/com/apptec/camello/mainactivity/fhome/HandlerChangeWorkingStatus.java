package com.apptec.camello.mainactivity.fhome;

import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.mainactivity.fhome.geofence.StartWorking;
import com.apptec.camello.mainactivity.fhome.geofence.StopWorking;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.util.Constants;

import timber.log.Timber;

/**
 * This class will is a Runnable that handles the working status change
 */
public class HandlerChangeWorkingStatus implements Runnable {

    // This instance of WorkZoneModel will be useful when the transition will be an enter
    private WorkZoneModel workZoneModel;

    // Listener
    private BaseProcessListener listener;

    /**
     * Constructor called when the transition will be an enter
     *
     * @param workZoneModel target work zone
     * @param listener      listener to the process
     */
    public HandlerChangeWorkingStatus(WorkZoneModel workZoneModel, BaseProcessListener listener) {
        this.workZoneModel = workZoneModel;
        this.listener = listener;
    }

    /**
     * Constructor when we don't know which is the work zone
     */
    public HandlerChangeWorkingStatus(BaseProcessListener listener) {
        this.listener = listener;
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


            new Thread(new StartWorking(workZoneModel, true, listener)).run();

        } else if (isWorking()) {
            /** If the user is working now, then finish whe work and create a new one */

            new Thread(new StopWorking(listener)).run();

        } else if (isNotInitWorking()) {
            /** If the period of working is not init start it*/
            new Thread(new StartWorking(workZoneModel, false, listener)).run();
        }

        Timber.i(RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().toString());
    }




    /**
     * Return true when the last working status was 'working'
     *
     * @return
     */
    @Deprecated
    private boolean isWorking() {

        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_WORKING_STATUS;
    }

    /**
     * Return true when the last working status was 'not init'
     *
     * @return
     */
    @Deprecated
    private boolean isNotInitWorking() {
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_NOT_INIT_STATUS;
    }







}
