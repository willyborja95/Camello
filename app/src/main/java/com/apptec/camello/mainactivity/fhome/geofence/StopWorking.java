package com.apptec.camello.mainactivity.fhome.geofence;


import com.apptec.camello.App;
import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.mainactivity.fhome.AssistanceRetrofitInterface;
import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Class runnable that set the corresponding values to variables to set the user as not working
 * It will stop tracking the geofencing points.
 * It will remove the geofencing
 */
public class StopWorking implements Runnable {

    // Listener
    private BaseProcessListener listener;


    /**
     * Ideal constructor
     *
     * @param listener listener of the process
     */
    public StopWorking(@Nullable BaseProcessListener listener) {
        this.listener = listener;
    }


    /**
     * Entrance point
     */
    @Override
    public void run() {

        if (listener != null) {
            listener.onProcessing();
        }
        Timber.i("Finishing job and creating a new one");

        // Change the status of the last working period to "finished" and the end time
        // to the current time

        // Get the last working period
        WorkingPeriodModel lastWorkingPeriod = RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod();

        // Update it
        lastWorkingPeriod.setStatus(Constants.INT_FINISHED_STATUS);
        lastWorkingPeriod.setEnd_date(System.currentTimeMillis());

        // Save the changes into database
        RoomHelper.getAppDatabaseInstance().workingPeriodDao().updateWorkingPeriod(lastWorkingPeriod);

        // Create a new working period with the status not init
        WorkingPeriodModel notInitWorkingPeriod = new WorkingPeriodModel(
                Constants.INT_NOT_INIT_STATUS // Not init status
        );

        // Save the new working period into database
        RoomHelper.getAppDatabaseInstance().workingPeriodDao().insert(notInitWorkingPeriod);


        // Stop tracking the work zone
        App.getGeofenceHelper().stopTracking();

        // Notify the server
        notifyTheServer();
    }


    /**
     * Notify the server
     */
    private void notifyTheServer() {
        Timber.d("Notify the server the user has change his working status");
        AssistanceRetrofitInterface retrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Call<GeneralResponse<JsonObject>> call;

        call = getCall(retrofitInterface);


        call.enqueue(new GeneralCallback<GeneralResponse<JsonObject>>(call) {
            @Override
            public void onResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                Timber.i("Assistance changed");
                Timber.i("Request code: %s", response.code());


                // Notify the listener
                if (listener != null) {
                    listener.onSuccessProcess();
                }
            }

            @Override
            public void onFinalFailure(Call<GeneralResponse<JsonObject>> call, Throwable t) {
                // Notify the listener
                if (listener != null) {
                    listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
                }
            }
        });

    }

    private Call<GeneralResponse<JsonObject>> getCall(@NotNull AssistanceRetrofitInterface retrofitInterface) {
        return retrofitInterface.registerAssistance(
                ApiClient.getAccessToken()
        );
    }


}
