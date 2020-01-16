package com.appTec.RegistrateApp.services.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appTec.RegistrateApp.models.User;

import java.io.File;

public class DatabaseAdapter {

    private static String DB_NAME = "registrateapp"; // Database name
    private static int DB_VERSION = 1; // Database version
    private File DB_FILE;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    private static DatabaseAdapter databaseAdapterInstance;

    private DatabaseAdapter(Context context) {
        this.context = context;
        sqLiteDatabase = new DatabaseHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static DatabaseAdapter getDatabaseAdapterInstance(Context context) {
        if (databaseAdapterInstance == null) {
            databaseAdapterInstance = new DatabaseAdapter(context);
        }
        return databaseAdapterInstance;
    }

    //LoginStatus database functions
    public boolean insertLoginStatus(int status) {
        int loginStatus = 0;
        Cursor cursor = sqLiteDatabase.query("LoginStatus", null, null, null, null, null, null);
        //If there isn't any row, the method add a new row, else update the status row.
        if (cursor != null & cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("status",status);
            return sqLiteDatabase.update("LoginStatus", contentValues, "id=1", null) > 0;
        } else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", status);
            return sqLiteDatabase.insert("LoginStatus", null, contentValues) > 0;
        }
    }

    public int getLoginStatus() {
        int loginStatus = 0;
        Cursor cursor = sqLiteDatabase.query("LoginStatus", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loginStatus = (cursor.getInt(1));
            }
        }
        return loginStatus;
    }

    //User database functions
    public boolean insertUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("names", user.getNames());
        contentValues.put("lastnames", user.getLastnames());
        contentValues.put("email", user.getEmail());
        return sqLiteDatabase.insert("User", null, contentValues) > 0;
    }

    public User getUser() {
        User user = new User();
        Cursor cursor = sqLiteDatabase.query("User", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user.setNames(cursor.getString(1));
                user.setLastnames(cursor.getString(2));
                user.setEmail(cursor.getString(3));
            }
        }
        return user;
    }

    public boolean deleteUser(){
        return sqLiteDatabase.delete("User", "", null)>0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
            super(context, databaseName, factory, dbVersion);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String loginStatusScript = "CREATE TABLE LoginStatus (id INTEGER primary key, status INTEGER)";
            String sqlUserScript =  "CREATE TABLE User (id INTEGER primary key, names TEXT not null, lastnames TEXT not null, email TEXT not null) ";
            sqLiteDatabase.execSQL(loginStatusScript);
            sqLiteDatabase.execSQL(sqlUserScript);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
            onCreate(sqLiteDatabase);
        }
    }


}