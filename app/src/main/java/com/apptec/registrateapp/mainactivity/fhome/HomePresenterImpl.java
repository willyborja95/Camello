package com.apptec.registrateapp.mainactivity.fhome;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;

public class HomePresenterImpl {

    /**
     * This class is in the middle of the MVVM pattern.
     * It interacts as an intermediary between the Model and the View.
     */

    public LiveData<WorkingPeriodModel> getLiveDataLastWorkingPeriod() {
        /**
         * Return the last working period saved
         */
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLiveDataLastWorkingPeriod();
    }

    public void changeLastWorkingStatus() {
        /**
         * Create a new thread and then run the process there
         */

        new Thread(new HandlerChangeWorkingStatus()).start();
    }
}
