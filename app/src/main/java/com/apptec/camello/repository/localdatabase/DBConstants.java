package com.apptec.camello.repository.localdatabase;

/**
 * This class will set constant values for the column names into database
 * <p>
 * I think the Android Jetpack should fix this
 * <p>
 * For avoid db problems, make sure that ANY NAME IS REPEATED
 */
public class DBConstants {

    public static final String DATABASE_NAME = "local_database.db";


    /**
     * Company model
     */
    public static final String COMPANY_TABLE = "company";
    public static final String COMPANY_PK = "company_id";
    public static final String COMPANY_NAME = "companyName";

    /**
     * Device model
     */
    public static final String DEVICE_TABLE = "device";
    public static final String DEVICE_PK = "device_id";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_MODEL = "device_model";
    public static final String DEVICE_IDENTIFIER = "device_identifier";
    public static final String DEVICE_ACTIVE = "device_active";
    public static final String DEVICE_FIREBASE_TOKEN = "device_token";
    public static final String DEVICE_PLATFORM = "device_platform";


    /**
     * Notification model
     */
    public static final String NOTIFICATION_TABLE = "notification";
    public static final String NOTIFICATION_PK = "notification_id";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_TEXT = "text";
    public static final String NOTIFICATION_EXPIRATION = "expiration_date";
    public static final String NOTIFICATION_SENT_DATE = "sent_date";
    public static final String NOTIFICATION_IS_READ = "is_read";


    /**
     * Permission model
     */
    public static final String PERMISSION_TABLE = "permission";
    public static final String PERMISSION_PK = "permission_id";
    public static final String PERMISSION_COMMENT = "comment";
    public static final String PERMISSION_PERMISSION_TYPE_FK = "type_fk";
    public static final String PERMISSION_PERMISSION_STATUS_FK = "status_fk";
    public static final String PERMISSION_START_DATE = "start_date";
    public static final String PERMISSION_END_DATE = "end_date";


    /**
     * Permission status model
     */
    public static final String PERMISSION_STATUS_TABLE = "pstatus";
    public static final String PERMISSION_STATUS_PK = "pstatus_id";
    public static final String PERMISSION_STATUS_NAME = "status_name";


    /**
     * Permission type model
     */
    public static final String PERMISSION_TYPE_TABLE = "ptype";
    public static final String PERMISSION_TYPE_PK = "ptype_id";
    public static final String PERMISSION_TYPE_NAME = "type_name";


    /**
     * User model
     */
    public static final String USER_TABLE = "user";
    public static final String USER_PK = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_LAST_NAME = "user_lastname";
    public static final String USER_EMAIL = "user_email";


    /**
     * Working period model
     */
    public static final String WORKING_PERIOD_TABLE = "period";
    public static final String WORKING_PERIOD_PK = "period_id";
    public static final String WORKING_PERIOD_START_DATE = "period_start_date";
    public static final String WORKING_PERIOD_END_DATE = "period_end_date";
    public static final String WORKING_PERIOD_STATUS = "period_status";
    public static final String WORKING_PERIOD_WORK_ZONE_FK = "work_zone";


    /**
     * Work zone model
     */
    public static final String WORK_ZONE_TABLE = "zone";
    public static final String WORK_ZONE_PK = "zone_id";
    public static final String WORK_ZONE_NAME = "zone_name";
    public static final String WORK_ZONE_LATITUDE = "lat";
    public static final String WORK_ZONE_LONGITUDE = "lng";
    public static final String WORK_ZONE_RADIUS = "radius";


}
