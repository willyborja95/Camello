package com.appTec.RegistrateApp.interactor;


import android.content.Context;

public interface HomeInteractor {
    /**
     * Interface of Home's interactor
     */

    void handleStartChronometer(Context context);   // Handle
    void chronometerStarted();       // Call the presenter


}
