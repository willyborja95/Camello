package com.appTec.RegistrateApp.presenter;


import android.content.Context;

public interface HomePresenter {
    /**
     * Interface oo Home's presenter
     */

    void handleStartChronometer(Context context);   // Calling the interactor
    void chronometerStarted();       // Calling the view



}
