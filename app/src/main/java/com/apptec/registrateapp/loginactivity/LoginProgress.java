package com.apptec.registrateapp.loginactivity;

import androidx.annotation.Nullable;


public class LoginProgress {
    /**
     * Authentication result : success (user details) or error message.
     */

    public static final int NOT_INIT = -3;
    public static final int SUCCESSFUL = 1;
    public static final int PROCESSING = 0;
    public static final int FAILED = -1;
    public static final int CANCELED = -2;


    @Nullable
    private int processStatus;


    @Nullable
    private Integer error, titleError;  // String resource

    public LoginProgress(@Nullable Integer titleError, @Nullable Integer error) {
        /**
         * Constructor used when an error happen
         */
        this.error = error;
        this.titleError = titleError;
        this.processStatus = LoginProgress.FAILED;
    }

    public LoginProgress(@Nullable int value) {
        this.processStatus = value;
    }

    @Nullable
    public int getProcessStatus() {
        return this.processStatus;
    }

    @Nullable
    Integer getTitleError() {
        return titleError;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public void setError(@Nullable Integer error) {
        this.error = error;
    }

    public void setTitleError(@Nullable Integer titleError) {
        this.titleError = titleError;
    }
}