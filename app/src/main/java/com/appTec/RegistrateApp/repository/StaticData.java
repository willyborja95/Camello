package com.appTec.RegistrateApp.repository;

import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;

import java.util.ArrayList;

public class StaticData {
    /**
     * This is a class when we can hold data for the app. In that way it can be access from anywhere of the app.
     */

    //

    private static User currentUser = new User();
    private static Company currentCompany = new Company();
    private static Device currentDevice = new Device();
    private static String currentImei = "NONE";
    private static ArrayList<PermissionType> permissionTypes= new ArrayList<>();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        StaticData.currentUser = currentUser;
    }

    public static Company getCurrentCompany() {
        return currentCompany;
    }

    public static void setCurrentCompany(Company currentCompany) {
        StaticData.currentCompany = currentCompany;
    }

    public static Device getCurrentDevice() {
        return currentDevice;
    }

    public static void setCurrentDevice(Device currentDevice) {
        StaticData.currentDevice = currentDevice;
    }

    public static String getCurrentImei() {
        return currentImei;
    }

    public static void setCurrentImei(String currentImei) {
        StaticData.currentImei = currentImei;
    }

    public static ArrayList<PermissionType> getPermissionTypes() {
        return permissionTypes;
    }

    public static void setPermissionTypes(ArrayList<PermissionType> permissionTypes) {
        StaticData.permissionTypes = permissionTypes;
    }

}
