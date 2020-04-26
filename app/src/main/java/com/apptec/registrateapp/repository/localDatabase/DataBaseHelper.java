package com.apptec.registrateapp.repository.localDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DataBaseHelper  extends SQLiteOpenHelper {
    /**
     * Singleton class, that means that will be only one instance of this class and could be access
     * by the method getInstance(...)
     */

    private static String DB_NAME = "registrateapp"; // Database name
    private static int DB_VERSION = 2; // Database version
    private File DB_FILE;


    // Singleton
    private static DataBaseHelper sInstance;

    private DataBaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        /**
         * Constructor should be private to prevent direct instantiation.
         * Make a call to the static method "getInstance()" instead.
         */
        super(context, databaseName, factory, dbVersion);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCompanyScript = "CREATE TABLE IF NOT EXISTS Company (id INTEGER primary key, name TEXT, latitude TEXT, longitude TEXT, radius REAL) ";
        String sqlUserScript = "CREATE TABLE IF NOT EXISTS User (id INTEGER primary key, names TEXT not null, lastnames TEXT not null, email TEXT not null) ";
        String sqlDeviceScript = "CREATE TABLE IF NOT EXISTS Device (id INTEGER primary key, name TEXT, model TEXT, imei TEXT, status INTEGER) ";
        String sqlPermissionTypeScript = "CREATE TABLE IF NOT EXISTS PermissionType (id INTEGER primary key, name TEXT) ";
        db.execSQL(sqlCompanyScript);
        db.execSQL(sqlUserScript);
        db.execSQL(sqlDeviceScript);
        db.execSQL(sqlPermissionTypeScript);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }



    public static synchronized DataBaseHelper getInstance(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        /**
         * Use the application context, which will ensure that you
         * don't accidentally leak an Activity's context.
         * See this article for more information: http://bit.ly/6LRzfx
         */

        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext(), databaseName, factory, dbVersion);
        }
        return sInstance;
    }

    public static synchronized DataBaseHelper getInstance(Context context) {
        /**
         * It return the singleton instance
         */
        return sInstance;
    }




}
