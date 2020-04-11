package com.appTec.RegistrateApp.interactor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.appTec.RegistrateApp.models.Assistance;
import com.appTec.RegistrateApp.repository.StaticData;
import com.appTec.RegistrateApp.repository.location.LocationHelper;
import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.AssistanceRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeInteractorImpl implements HomeInteractor {
    /**
     * Implementation of Home's interactor
     */

    @Override
    public void handleStartChronometer(Context context) {
        /**
         * ToDo:
         * Here we do:
         * - ToDo: Review the internet connection
         * - ToDo: If the last time exit is not synchronized. Synchronize it.
         * - ToDo: Send the time to the server
         */

        // ToDo: If the last time exit is not synchronized. Synchronize it.
        if(isStateOutOfSync(context)){
            syncState();
        }

        // Send the time to the server
        AssistanceRetrofitInterface assistanceRetrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Assistance assistanceLog = new Assistance(
                SharedPreferencesHelper.getSharedPreferencesInstance(context).getInt(StaticData.getCurrentImei(), 0),
                LocationHelper.getLocationHelper().getLatitude(),
                LocationHelper.getLocationHelper().getLongitude());
        Call<JsonObject> assistanceCall = assistanceRetrofitInterface.post(ApiClient.getToken(), assistanceLog);

    }

    private void syncState() {
    }

    private boolean isStateOutOfSync(Context context) {
        /**
         * Review if the last time when the user exited was no uploaded to the server
         */
        String result = SharedPreferencesHelper.getSharedPreferencesInstance(context).getString(Constants.STATE_WORKING, "");
        return result.equals(Constants.STATE_WORKING);
    }

    @Override
    public void chronometerStarted() {
        /**
         *
         */

    }


    private boolean isNetworkAvailable(){
        /**
         * Todo
         */
        return true;
    }

}
