package com.apptec.camello.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

/**
 * Dao for work zone model
 */
@Dao
public interface WorkZoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(WorkZoneModel workZone);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOrReplace(List<WorkZoneModel> workZoneModels);

    @Delete
    void delete(WorkZoneModel workZone);

    @Query("SELECT * FROM " + DBConstants.WORK_ZONE_TABLE)
    List<WorkZoneModel> getListWorkZones();

    @Query("Delete FROM " + DBConstants.WORK_ZONE_TABLE)
    void deleteAll();
}
