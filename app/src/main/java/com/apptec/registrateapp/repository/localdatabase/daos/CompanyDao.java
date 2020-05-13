package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.apptec.registrateapp.models.CompanyModel;

@Dao
public interface CompanyDao {
    /**
     * Dao fro company
     * @return
     */


    @Insert
    void insert(CompanyModel company);

    @Delete
    void delete(CompanyModel company);

}
