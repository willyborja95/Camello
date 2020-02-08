package com.appTec.RegistrateApp.services.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;

import java.io.File;
import java.util.ArrayList;

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
            contentValues.put("status", status);
            return sqLiteDatabase.update("LoginStatus", contentValues, "id=1", null) > 0;
        } else {
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
        contentValues.put("id", user.getId());
        contentValues.put("names", user.getNombres());
        contentValues.put("lastnames", user.getApellidos());
        contentValues.put("email", user.getEmail());
        return sqLiteDatabase.insert("User", null, contentValues) > 0;
    }

    public User getUser() {
        User user = new User();
        Cursor cursor = sqLiteDatabase.query("User", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user.setId(cursor.getInt(0));
                user.setNombres(cursor.getString(1));
                user.setApellidos(cursor.getString(2));
                user.setEmail(cursor.getString(3));
            }
        }
        return user;
    }

    public boolean deleteUser() {
        return sqLiteDatabase.delete("User", "", null) > 0;
    }

    //Device database functions
    public boolean insertDevice(Device device) {
        deleteDevice();
        int deviceStatus = 0;
        if (device.isStatus() == false) {
            deviceStatus = 0;
        } else {
            deviceStatus = 1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", device.getId());
        contentValues.put("name", device.getNombre());
        contentValues.put("model", device.getModelo());
        contentValues.put("imei", device.getImei());
        contentValues.put("status", deviceStatus);
        return sqLiteDatabase.insert("Device", null, contentValues) > 0;
    }

    public Device getDevice() {
        Device device = null;
        Cursor cursor = sqLiteDatabase.query("Device", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                device = new Device();
                int deviceStatus = cursor.getInt(4);
                device.setId(cursor.getInt(0));
                device.setNombre(cursor.getString(1));
                device.setModelo(cursor.getString(2));
                device.setImei(cursor.getString(3));
                if (deviceStatus == 0) {
                    device.setStatus(false);
                } else {
                    device.setStatus(true);
                }
            }
        }
        return device;
    }

    public boolean deleteDevice() {
        return sqLiteDatabase.delete("Device", "", null) > 0;
    }

    //Company database functions
    public boolean insertCompany(Company company){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", company.getName());
        contentValues.put("latitude", company.getLatitude());
        contentValues.put("longitude", company.getLongitude());
        contentValues.put("radius", company.getRadius());
        return sqLiteDatabase.insert("Company", null, contentValues) > 0;
    }

    public Company getCompany(){
        Company company = new Company();
        Cursor cursor = sqLiteDatabase.query("Company", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(1);
                Double latitude = cursor.getDouble(2);
                Double longitude = cursor.getDouble(3);
                Double radius = cursor.getDouble(4);
                company.setName(name);
                company.setLatitude(latitude);
                company.setLongitude(longitude);
                company.setRadius(radius);
            }
        }
        return company;
    }

    //PermissionType database functions
    public boolean insertPermissionType(PermissionType permissionType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", permissionType.getId());
        contentValues.put("name", permissionType.getNombe());
        return sqLiteDatabase.insert("PermissionType", null, contentValues) > 0;
    }

    public ArrayList<PermissionType> getPermissionType() {
        ArrayList<PermissionType> lstPermissionType = new ArrayList<PermissionType>();
        Cursor cursor = sqLiteDatabase.query("PermissionType", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int permissionTypeId = cursor.getInt(0);
                String permissionTypeName = cursor.getString(1);
                PermissionType permissionType = new PermissionType(permissionTypeId, permissionTypeName);
                lstPermissionType.add(permissionType);
            }
        }
        return lstPermissionType;
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
            String sqlCompanyScript = "CREATE TABLE Company (id INTEGER primary key, name TEXT, latitude REAL, longitude REAL, radius REAL) ";
            String sqlUserScript = "CREATE TABLE User (id INTEGER primary key, names TEXT not null, lastnames TEXT not null, email TEXT not null) ";
            String sqlDeviceScript = "CREATE TABLE Device (id INTEGER primary key, name TEXT, model TEXT, imei TEXT, status INTEGER) ";
            String sqlPermissionTypeScript = "CREATE TABLE PermissionType (id INTEGER primary key, name TEXT) ";
            sqLiteDatabase.execSQL(loginStatusScript);
            sqLiteDatabase.execSQL(sqlCompanyScript);
            sqLiteDatabase.execSQL(sqlUserScript);
            sqLiteDatabase.execSQL(sqlDeviceScript);
            sqLiteDatabase.execSQL(sqlPermissionTypeScript);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
            onCreate(sqLiteDatabase);
        }
    }


}