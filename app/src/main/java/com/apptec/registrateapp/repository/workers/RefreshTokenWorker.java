package com.apptec.registrateapp.repository.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webServices.ApiClient;
import com.apptec.registrateapp.repository.webServices.interfaces.AuthInterface;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefreshTokenWorker extends Worker {

    public RefreshTokenWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        /** Initializing credentials manager code goes here **/


        String refreshToken = ApiClient.getRefreshToken();
        String accessToken = ApiClient.getAccessToken();

        // I do not know if is the best way to put this inside


        AuthInterface authInterface = ApiClient.getClient().create(AuthInterface.class);
        Call<JsonObject> refreshCall = authInterface.refreshToken(ApiClient.getAccessToken(), ApiClient.getRefreshToken());
        refreshCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    // Get the new access token
                    String newAccessToken = response.body().get("accessToken").getAsString();
                    SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, newAccessToken); // Storage in shared preferences

                    // TODO: Return success
                } catch (NullPointerException npe) {
                    // TODO: Handle this

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO: Return failure
            }
        });



        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }


}