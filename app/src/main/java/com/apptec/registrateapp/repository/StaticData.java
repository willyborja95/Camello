package com.apptec.registrateapp.repository;

import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.models.UserModel;

import java.util.ArrayList;

public class StaticData {
    /**
     * This is a class when we can hold data for the app. In that way it can be access from anywhere of the app.
     * Maybe it can be replaced by the view models
     */


    private static UserModel currentUser = new UserModel();
    private static DeviceModel currentDevice = new DeviceModel();
    private static ArrayList<PermissionType> permissionTypes= new ArrayList<>();

    public static UserModel getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserModel currentUser) {
        StaticData.currentUser = currentUser;
    }

    public static DeviceModel getCurrentDevice() {
        return currentDevice;
    }

    public static void setCurrentDevice(DeviceModel currentDevice) {
        StaticData.currentDevice = currentDevice;
    }


    public static ArrayList<PermissionType> getPermissionTypes() {
        return permissionTypes;
    }

    public static void setPermissionTypes(ArrayList<PermissionType> permissionTypes) {
        StaticData.permissionTypes = permissionTypes;
    }

}
