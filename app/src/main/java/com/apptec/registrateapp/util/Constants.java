package com.apptec.registrateapp.util;

public class Constants {
    /**
     * Here goes the constants for no hardcoding
     * */


    // Urls
    public static final String BASE_URL = "https://registrate-staging.herokuapp.com";
    public static final String NOTIFICATIONS_URL = "notifications";
    public static final String LOGIN_URL = "/api/auth/login";
    public static final String DEVICE_URL = "/api/employee";  // api/emplyee/{{userid}}/devices/


    // String messages (Logs)
    public static final String UPDATING_CHANGES = "Actualizando datos";


    // Shared preferences key words values
    public static final String SHARED_PREFERENCES_GLOBAL = "com.appTec.RegistrateApp.PREFERENCES_FILE_KEY"; // Storage


    public static final String LAST_EXIT_TIME = "LAST_EXIT_TIME";                                   //

    public static final String APP_IS_RUNNING_BY_FIRST_TIME = "IS_FIRST_TIME";                      // Storage a boolean
    public static final String USER_ACCESS_TOKEN = "ACCESS_TOKEN";                                  // Storage a string
    public static final String USER_REFRESH_TOKEN = "REFRESH_TOKEN";                                // Storage a string
    public static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";                                   // Storage a string
    public static final String CURRENT_IMEI = "CURRENT_IMEI";                                       // Storage a string
    public static final String IS_USER_WORKING = "IS_WORKING";                                      // Storage a boolean
    public static final String IS_USER_LOGGED = "IS_LOGGED";                                        // Storage a boolean


    // API constants
    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String ENDPOINT_FIREBASE = "firebase_token/";


    //
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // Local Database constants
    public static final String DATABASE_NAME = "local_database";

}
