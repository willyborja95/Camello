package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

@Dao
public interface WorkingPeriodDao {
    /**
     * Dao for working period
     */

    @Query("SELECT * FROM " + DBConstants.WORKING_PERIOD_TABLE + " WHERE " + DBConstants.WORKING_PERIOD_PK +
            "=(Select MAX(" + DBConstants.WORKING_PERIOD_PK + ") FROM " + DBConstants.WORKING_PERIOD_TABLE + ")")
    LiveData<WorkingPeriodModel> getLiveDataLastWorkingPeriod();

    @Query("SELECT * FROM " + DBConstants.WORKING_PERIOD_TABLE + " ORDER BY " + DBConstants.WORKING_PERIOD_PK +
            " DESC LIMIT 1")
    WorkingPeriodModel getLastWorkingPeriod();

    @Insert
    void insert(WorkingPeriodModel workingPeriod);

    @Delete
    void delete(WorkingPeriodModel workingPeriod);

    @Query("UPDATE " + DBConstants.WORKING_PERIOD_TABLE + " SET " + DBConstants.WORKING_PERIOD_STATUS + "=:status " +
            "WHERE " + DBConstants.WORKING_PERIOD_PK + "=(Select MAX(" + DBConstants.WORKING_PERIOD_PK + ") " +
            "FROM " + DBConstants.WORKING_PERIOD_TABLE + ")")
    void changeLastWorkingPeriod(int status);
}
