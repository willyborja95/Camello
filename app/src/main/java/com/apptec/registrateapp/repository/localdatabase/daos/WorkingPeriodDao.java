package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.WorkingPeriod;

@Dao
public interface WorkingPeriodDao {
    /**
     * Dao for working period
     */

    @Query("SELECT * FROM workingperiod WHERE id=(Select MAX(id) FROM workingperiod)")
    LiveData<WorkingPeriod> getLiveDataLastWorkingPeriod();

    @Query("SELECT * FROM workingperiod ORDER BY id DESC LIMIT 1")
    WorkingPeriod getLastWorkingPeriod();

    @Insert
    void insert(WorkingPeriod workingPeriod);

    @Delete
    void delete(WorkingPeriod workingPeriod);

    @Query("UPDATE workingperiod SET status=:status WHERE id=(Select MAX(id) FROM workingperiod)")
    void changeLastWorkingPeriod(int status);
}
