package com.apptec.camello.mainactivity.fhome;

import androidx.lifecycle.LiveData;

import com.apptec.camello.App;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;

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

    public void changeLastWorkingStatus(WorkZoneModel workZoneModel) {
        App.changeWorkStatus(workZoneModel);
    }
}
