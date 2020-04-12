package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home;


import android.content.Context;

public interface HomeView {
    /**
     * Interface for the Home view.
     * Here we listen the interaction from the user with the chronometer
     */

    void handleStartChronometer(Context context);   // When the user press 'start'. Passed to presenter.
    void chronometerStarted();       // The UI changes when the chronometer has started. From the presenter.

    void onLocationChanged();       // When the user goes out.

}
