package com.apptec.camello.util;

import androidx.annotation.Nullable;

import com.apptec.camello.R;

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
    private Integer message, titleMessage;  // String resource

    /**
     * Constructor used when a the process end
     *
     * @param value
     */
    public Process(@Nullable int value, @Nullable Integer titleMessage, @Nullable Integer message) {
        this.processStatus = value;

        if (titleMessage != null) {
            this.titleMessage = titleMessage;
        } else {
            this.resolveEmptyTitleMessage(this.processStatus);
        }
        if (message != null) {
            this.message = message;
        } else {
            this.resolveEmptyMessage(this.processStatus);
        }


    }

    /**
     * Set a default title message for the process status
     *
     * @param processStatus this process status
     */
    private void resolveEmptyTitleMessage(int processStatus) {
        switch (processStatus) {
            case NOT_INIT:
                this.titleMessage = R.string.process_not_init_default_title_message;
                break;
            case SUCCESSFUL:
                this.titleMessage = R.string.process_successful_default_title_message;
                break;
            case PROCESSING:
                this.titleMessage = R.string.process_processing_default_title_message;
                break;
            case FAILED:
                this.titleMessage = R.string.process_failed_default_title_message;
                break;
            case CANCELED:
                this.titleMessage = R.string.process_canceled_default_title_message;
                break;
        }
    }

    /**
     * Set a default message for the process status
     *
     * @param processStatus this process status
     */
    private void resolveEmptyMessage(int processStatus) {
        switch (processStatus) {
            case NOT_INIT:
                this.message = R.string.process_not_init_default_message;
                break;
            case SUCCESSFUL:
                this.message = R.string.process_successful_default_tmessage;
                break;
            case PROCESSING:
                this.message = R.string.process_processing_default_message;
                break;
            case FAILED:
                this.message = R.string.process_failed_default_message;
                break;
            case CANCELED:
                this.message = R.string.process_canceled_default_message;
                break;
        }
    }

    @Nullable
    public Integer getProcessStatus() {
        return this.processStatus;
    }

    @Nullable
    public Integer getTitleMessage() {
        return titleMessage;
    }

    @Nullable
    public Integer getMessage() {
        return message;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public void setMessage(@Nullable Integer message) {
        this.message = message;
    }

    public void setTitleMessage(@Nullable Integer titleMessage) {
        this.titleMessage = titleMessage;
    }


    public void errorOccurred(int title, int message) {
        this.titleMessage = title;
        this.message = message;
        this.processStatus = Process.FAILED;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return processStatus == process.processStatus &&
                Objects.equals(message, process.message) &&
                Objects.equals(titleMessage, process.titleMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processStatus, message, titleMessage);
    }

    @Override
    public String toString() {
        return "Process{" +
                "processStatus=" + processStatus +
                ", message=" + message +
                ", titleMessage=" + titleMessage +
                '}';
    }
}
