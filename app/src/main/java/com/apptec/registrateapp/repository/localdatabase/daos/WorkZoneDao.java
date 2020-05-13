package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.apptec.registrateapp.models.WorkZoneModel;

@Dao
public interface WorkZoneDao {
    /**
     * Dao for work zone model
     */

    @Insert
    void insert(WorkZoneModel workZone);

    @Delete
    void delete(WorkZoneModel workZone);


}
