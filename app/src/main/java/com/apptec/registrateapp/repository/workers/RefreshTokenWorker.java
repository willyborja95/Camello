package com.apptec.registrateapp.repository.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webServices.ApiClient;
import com.apptec.registrateapp.repository.webServices.interfaces.AuthInterface;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RefreshTokenWorker extends Worker {

    private final String TAG = "RefreshTokenWorker";

    public RefreshTokenWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        /**
         * Initializing credentials manager code goes here
         * */
        Log.d(TAG, "Init doWork");
        AuthInterface authInterface = ApiClient.getClient().create(AuthInterface.class);
        Call<JsonObject> refreshCall = authInterface.refreshToken(
                new RefreshTokenBody(ApiClient.getAccessToken(), ApiClient.getRefreshToken())
        );

        try {
            // With do not enqueue the call because it is no necessary an immediate response from this worker
            Response<JsonObject> response = refreshCall.execute();
            // Get the new access token
            String newAccessToken = response.body().get("data").getAsJsonObject().get("accessToken").getAsString();
            SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, newAccessToken); // Storage in shared preferences

            Log.i(TAG, "Token refreshed");
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "Refresh token failure: " + e.getMessage());
            return Result.failure();
        }



        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }


}