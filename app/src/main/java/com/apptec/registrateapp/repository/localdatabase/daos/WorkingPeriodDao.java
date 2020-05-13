package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.WorkingPeriodModel;

@Dao
public interface WorkingPeriodDao {
    /**
     * Dao for working period
     */

    @Query("SELECT * FROM WorkingPeriodModel WHERE id=(Select MAX(id) FROM WorkingPeriodModel)")
    LiveData<WorkingPeriodModel> getLiveDataLastWorkingPeriod();

    @Query("SELECT * FROM WorkingPeriodModel ORDER BY id DESC LIMIT 1")
    WorkingPeriodModel getLastWorkingPeriod();

    @Insert
    void insert(WorkingPeriodModel workingPeriod);

    @Delete
    void delete(WorkingPeriodModel workingPeriod);

    @Query("UPDATE WorkingPeriodModel SET status=:status WHERE id=(Select MAX(id) FROM WorkingPeriodModel)")
    void changeLastWorkingPeriod(int status);
}
