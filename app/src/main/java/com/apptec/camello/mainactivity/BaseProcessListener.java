package com.apptec.camello.mainactivity;

import androidx.annotation.Nullable;

/**
 * Interface that will use as reference many process of this app
 */
public interface BaseProcessListener {

    /**
     * Method to show an error if it happens in some process
     *
     * @param title   not null string id resource
     * @param message not null string id resource
     */
    void onErrorOccurred(int title, int message);

    /**
     * The process is running
     *
     * @param title   may be null
     * @param message may be null
     */
    void onProcessing(@Nullable Integer title, @Nullable Integer message);

    /**
     * Process finished right
     *
     * @param title   may be null
     * @param message may be null
     */
    void onSuccessProcess(@Nullable Integer title, @Nullable Integer message);


}
