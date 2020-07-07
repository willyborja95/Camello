package com.apptec.camello.mainactivity;

import androidx.annotation.Nullable;

public class Process implements BaseProcessListener {
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

    /**
     * Method to show an error if it happens in some process
     *
     * @param title
     * @param message
     */
    @Override
    public void onErrorOccurred(int title, int message) {
        this.error = title;
        this.titleError = message;
        this.processStatus = Process.FAILED;
    }

    /**
     * The process is running
     */
    @Override
    public void onProcessing() {
        this.setProcessStatus(Process.PROCESSING);
    }

    /**
     * Process finished right
     */
    @Override
    public void onSuccessProcess() {
        this.setProcessStatus(Process.SUCCESSFUL);
    }
}
