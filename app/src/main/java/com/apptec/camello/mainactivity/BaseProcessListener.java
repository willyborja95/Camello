package com.apptec.camello.mainactivity;

/**
 * Interface that will use as reference many process of this app
 */
public interface BaseProcessListener {

    /**
     * Method to show an error if it happens in some process
     */
    public void onErrorOccurred(int title, int message);

    /**
     * The process is running
     */
    public void onProcessing();

    /**
     * Process finished right
     */
    public void onSuccessProcess();


}
