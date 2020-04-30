package com.apptec.registrateapp.repository.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apptec.registrateapp.repository.webServices.ApiClient;

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
        // TODO: Verify this

        String existingTokenId = ApiClient.getRefreshToken();


        // TODO:


        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }


}