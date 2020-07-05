package com.apptec.camello.localdatabe;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.apptec.camello.App;
import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.AppDatabase;
import com.apptec.camello.repository.localdatabase.daos.WorkingPeriodDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

@RunWith(AndroidJUnit4.class)
public class WorkingPeriodTest {

    // Test the permission status database
    private WorkingPeriodDao workingPeriodDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = App.getContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        workingPeriodDao = db.workingPeriodDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void readInList() throws Exception {


        List<WorkingPeriodModel> workingPeriodModels = workingPeriodDao.getAll();
        Timber.d("List size " + workingPeriodModels.size());
        for (int i = 0; i < workingPeriodModels.size(); i++) {
            Timber.d(workingPeriodModels.get(i).toString());

        }

    }
}
