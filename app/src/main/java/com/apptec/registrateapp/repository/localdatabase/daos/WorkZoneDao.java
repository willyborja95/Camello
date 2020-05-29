package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.util.List;


@Dao
public interface WorkZoneDao {
    /**
     * Dao for work zone model
     */

    @Insert
    void insert(WorkZoneModel workZone);

    @Delete
    void delete(WorkZoneModel workZone);

    @Query("SELECT * FROM " + DBConstants.WORK_ZONE_TABLE)
    List<WorkZoneModel> getListWorkZones();

    @Query("Delete FROM " + DBConstants.WORK_ZONE_TABLE)
    void deleteAll();
}
