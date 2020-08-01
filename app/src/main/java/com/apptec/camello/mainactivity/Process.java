package com.apptec.camello.mainactivity;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Authentication result : success (user details) or error message.
 */
public class Process {

    public static final int NOT_INIT = -3;
    public static final int SUCCESSFUL = 1;
    public static final int PROCESSING = 0;
    public static final int FAILED = -1;
    public static final int CANCELED = -2;


    @Nullable
    private int processStatus;


    @Nullable
    private Integer error, titleError;  // String resource

    /**
     * Constructor used when a error happen
     *
     * @param titleError title to be show in a dialog
     * @param error      text to be show in a dialog
     */
    public Process(@Nullable Integer titleError, @Nullable Integer error) {
        this.error = error;
        this.titleError = titleError;
        this.processStatus = Process.FAILED;
    }

    /**
     * Constructor used when a the process end
     *
     * @param value
     */
    public Process(@Nullable int value) {
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


    public void errorOccurred(int title, int message) {
        this.titleError = title;
        this.error = message;
        this.processStatus = Process.FAILED;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return processStatus == process.processStatus &&
                Objects.equals(error, process.error) &&
                Objects.equals(titleError, process.titleError);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processStatus, error, titleError);
    }
}
