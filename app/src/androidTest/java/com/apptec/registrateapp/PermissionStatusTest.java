package com.apptec.registrateapp;


import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.apptec.registrateapp.models.PermissionStatus;
import com.apptec.registrateapp.repository.localdatabase.AppDatabase;
import com.apptec.registrateapp.repository.localdatabase.daos.PermissionStatusDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

@RunWith(AndroidJUnit4.class)
public class PermissionStatusTest {

    // Test the permission status database
    private PermissionStatusDao permissionStatusDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = App.getContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        permissionStatusDao = db.permissionStatusDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void readInList() throws Exception {


        List<PermissionStatus> permissionStatusList = permissionStatusDao.getListPermission();
        Timber.d("List size " + permissionStatusList.size());
        for (int i = 0; i < permissionStatusList.size(); i++) {
            Timber.wtf(permissionStatusList.get(i).getStatusName());

        }

    }
}