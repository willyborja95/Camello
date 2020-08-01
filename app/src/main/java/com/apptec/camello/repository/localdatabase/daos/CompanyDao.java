package com.apptec.camello.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.CompanyModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

/**
 * Dao fro company
 */
@Dao
public interface CompanyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(CompanyModel company);

    @Delete
    void delete(CompanyModel company);

    @Query("DELETE FROM " + DBConstants.COMPANY_TABLE)
    void deleteAll();


}
