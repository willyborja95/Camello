package com.apptec.camello.mainactivity.fhome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Body for register an attendance
 */
public class AssistanceBody {


    @NonNull
    @SerializedName("deviceId")
    private int deviceId;

    @Nullable
    @SerializedName("workzoneId")
    private int workzoneId;

    /**
     * Constructor
     *
     * @param deviceId   current id of this device
     * @param workzoneId current work zone id
     */
    public AssistanceBody(int deviceId, int workzoneId) {
        this.deviceId = deviceId;
        this.workzoneId = workzoneId;
    }

}
