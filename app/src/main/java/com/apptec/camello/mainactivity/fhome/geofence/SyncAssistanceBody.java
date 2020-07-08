package com.apptec.camello.mainactivity.fhome.geofence;

import androidx.annotation.NonNull;

/**
 * Body for syn the assistance
 */
public class SyncAssistanceBody {

    @NonNull
    String exit_time;

    // Setter and getters
    @NonNull
    public String getExit_time() {
        return exit_time;
    }

    public void setExit_time(@NonNull String exit_time) {
        this.exit_time = exit_time;
    }
}
