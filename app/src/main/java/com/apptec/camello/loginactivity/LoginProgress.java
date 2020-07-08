package com.apptec.camello.loginactivity;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginProgress {


    public static final int NOT_INIT = -3;
    public static final int SUCCESSFUL = 1;
    public static final int PROCESSING = 0;
    public static final int FAILED = -1;


    @Nullable
    private int processStatus;


    @Nullable
    private Integer error, titleError;  // String resource

    /**
     * Constructor used when an error happen
     */
    public LoginProgress(@Nullable Integer titleError, @Nullable Integer error) {

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

    @Override
    public String toString() {
        return "LoginProgress{" +
                "processStatus=" + processStatus +
                ", error=" + error +
                ", titleError=" + titleError +
                '}';
    }
}