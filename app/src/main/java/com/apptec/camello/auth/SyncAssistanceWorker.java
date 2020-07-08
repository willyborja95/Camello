package com.apptec.camello.auth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apptec.camello.mainactivity.fhome.AssistanceRetrofitInterface;
import com.apptec.camello.mainactivity.fhome.geofence.SyncAssistanceBody;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class SyncAssistanceWorker extends Worker {


    /**
     * Required construcor
     *
     * @param context
     * @param workerParams
     */
    public SyncAssistanceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Override this method to do your actual background processing.  This method is called on a
     * background thread - you are required to <b>synchronously</b> do your work and return the
     * {@link Result} from this method.  Once you return from this
     * method, the Worker is considered to have finished what its doing and will be destroyed.
     */
    @NonNull
    @Override
    public Result doWork() {
        Timber.d("Started to sync assistance");
        AssistanceRetrofitInterface retrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);

        // Create the body
        SyncAssistanceBody body = new SyncAssistanceBody();
        body.setExit_time(DateConverter.toStringDateFormat(SharedPreferencesHelper.getSharedPreferencesInstance().getLong(Constants.LAST_EXIT_TIME, 0)));


        Call<GeneralResponse<JsonObject>> call = retrofitInterface.syncAssistance(
                ApiClient.getAccessToken(),
                body
        );

        try {
            // With do not enqueue the call because it is no necessary an immediate response from this worker
            Response<GeneralResponse<JsonObject>> response = call.execute();

            if (response.isSuccessful()) {
                Timber.d("Sync assistance successful");
                return Result.success();

            }

        } catch (Exception e) {
            Timber.d("Failed to sync assistance");
            Timber.e(e);
            return Result.failure();
        }


        // Indicate whether the task finished successfully with the Result
        Timber.e("Failed to sync assistance");
        return Result.failure();
    }
}
