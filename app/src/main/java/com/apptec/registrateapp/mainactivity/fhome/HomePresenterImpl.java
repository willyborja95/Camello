package com.apptec.registrateapp.mainactivity.fhome;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;

/**
 * This class is in the middle of the MVVM pattern.
 * It interacts as an intermediary between the Model and the View.
 */
public class HomePresenterImpl {


    /**
     * @return the last working period saved
     */
    public LiveData<WorkingPeriodModel> getLiveDataLastWorkingPeriod() {

        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLiveDataLastWorkingPeriod();
    }

    /**
     * Create a new thread and then run the process there
     */
    public void changeLastWorkingStatus() {

        App.changeWorkStatus();
    }
}
