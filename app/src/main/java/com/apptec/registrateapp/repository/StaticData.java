package com.apptec.registrateapp.repository;

import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.models.User;

import java.util.ArrayList;

public class StaticData {
    /**
     * This is a class when we can hold data for the app. In that way it can be access from anywhere of the app.
     */

    //

    private static User currentUser = new User();
    private static Device currentDevice = new Device();
    private static ArrayList<PermissionType> permissionTypes= new ArrayList<>();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        StaticData.currentUser = currentUser;
    }

    public static Device getCurrentDevice() {
        return currentDevice;
    }

    public static void setCurrentDevice(Device currentDevice) {
        StaticData.currentDevice = currentDevice;
    }


    public static ArrayList<PermissionType> getPermissionTypes() {
        return permissionTypes;
    }

    public static void setPermissionTypes(ArrayList<PermissionType> permissionTypes) {
        StaticData.permissionTypes = permissionTypes;
    }

}
