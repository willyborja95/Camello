package com.apptec.registrateapp.presenter;

import com.apptec.registrateapp.interactor.MainInteractorImpl;

public class MainPresenterImpl {
    /**
     * This class will help the main activity
     */

    MainInteractorImpl mainInteractor;


    public boolean isTheFirstLogin(){
        /**
         * Calling the interactor
         */
        return mainInteractor.isTheFirstLogin();
    }

    public boolean isTheLoginFromTheSameUser(){
        /**
         * Calling the interactor
         */
        return mainInteractor.isTheLoginFromTheSameUser();
    }

    public void handleFirstLogin(){
        /**
         * Calling the interactor
         */

        mainInteractor.handleFirstLogin();


    }
}
