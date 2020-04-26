package com.apptec.registrateapp.repository.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.Company;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.models.User;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseAdapter {

    private static String DB_NAME = "registrateapp"; // Database name
    private static int DB_VERSION = 1; // Database version
    private File DB_FILE;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    private static DatabaseAdapter databaseAdapterInstance;

    private DatabaseAdapter() {
        this.context = App.getContext();
        sqLiteDatabase = DataBaseHelper.getInstance(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static DatabaseAdapter getDatabaseAdapterInstance() {
        if (databaseAdapterInstance == null) {
            databaseAdapterInstance = new DatabaseAdapter();
        }
        return databaseAdapterInstance;
    }

    //User database functions
    public boolean insertUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", user.getId());
        contentValues.put("names", user.getName());
        contentValues.put("lastnames", user.getLastName());
        contentValues.put("email", user.getEmail());
        return sqLiteDatabase.insert("User", null, contentValues) > 0;
    }

    public User getUser() {
        User user = new User();
        Cursor cursor = sqLiteDatabase.query("User", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
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
        contentValues.put("name", device.getName());
        contentValues.put("model", device.getModel());
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
                device.setName(cursor.getString(1));
                device.setModel(cursor.getString(2));
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

    public Company getCompany(){
        Company company = new Company();
        Cursor cursor = sqLiteDatabase.query("Company", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(1);
                Double latitude = Double.valueOf(cursor.getString(2));
                Double longitude = Double.valueOf(cursor.getString(3));
                Double radius = cursor.getDouble(4);
                company.setCompanyName(name);

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

    public void removeData() {
        sqLiteDatabase.delete("Company", "", null);
        sqLiteDatabase.delete("User", "", null);
        sqLiteDatabase.delete("Device", "", null);
        sqLiteDatabase.delete("PermissionType", "", null);
    }


    // Insert notification
    public boolean insertNotification(Notification notification){
        /**
         * It is messy
         */
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", notification.getTitle());
        contentValues.put("text", notification.getText());
        contentValues.put("expirationDate", notification.getExpirationDate().toString());
        contentValues.put("sentDate", notification.getSentDate().toString());
        return sqLiteDatabase.insert("Notification", null, contentValues) > 0;
    }

    public ArrayList<Notification> getNotifications(){
        /**
         * It is messy
         */
        ArrayList<Notification> notifications = new ArrayList<Notification>();
        Cursor cursor = sqLiteDatabase.query("Notification", null, null, null, null, null, null);
        if (cursor != null & cursor.getCount() > 0){
            while (cursor.moveToNext()){
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String title = cursor.getString(0);
                String text = cursor.getString(1);
                Date expirationDate = Date.valueOf(cursor.getString(2));
                Date sentDate = Date.valueOf(cursor.getString(3));

                //Notification notification = new Notification(title, text, expirationDate, sentDate);

                //notifications.add(notification);
            }

        }
        return notifications;

    }


}