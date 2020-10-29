package com.apptec.camello.mainactivity.fhome.geofence;


import com.apptec.camello.App;
import com.apptec.camello.R;
import com.apptec.camello.auth.AuthHelper;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.mainactivity.fhome.AssistanceBody;
import com.apptec.camello.mainactivity.fhome.AssistanceRetrofitInterface;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
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
            listener.onProcessing(null, null);
        }
        Timber.i("Finishing job and creating a new one");


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


            /**
             * Method that will be called after the onResponse() default method after doing some validations
             * * see {@link GeneralCallback}
             * This need to be override by the classes that implement GeneralCallback
             *
             * @param call     call
             * @param response response
             */
            @Override
            public void onFinalResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                Timber.i("Assistance changed");
                Timber.i("Request code: %s", response.code());

                SharedPreferencesHelper.putBooleanValue(Constants.NEED_SYNC_ASSISTANCE, false);

                // Notify the listener
                if (listener != null) {
                    listener.onSuccessProcess(R.string.exit_has_been_registered, null);
                }
            }

            /**
             * On final failure we save the last time when the user exit for sync when internet is available
             *
             * @param call call
             * @param t    throwable
             */
            @Override
            public void onFinalFailure(Call<GeneralResponse<JsonObject>> call, Throwable t) {
                Timber.d("Save the last time when the user exit for sync after");
                // Save stop working time to syn after
                SharedPreferencesHelper.putBooleanValue(Constants.NEED_SYNC_ASSISTANCE, true);
                SharedPreferencesHelper.putLongValue(Constants.LAST_EXIT_TIME, System.currentTimeMillis());

                scheduleJob();


                // Notify the listener
                if (listener != null) {
                    listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
                }
            }
        });

    }

    /**
     * Schedule the job for sync the time when the user exit
     */
    private void scheduleJob() {
        AuthHelper.scheduleSync();

    }

    private Call<GeneralResponse<JsonObject>> getCall(@NotNull AssistanceRetrofitInterface retrofitInterface) {

        // Create the body
        AssistanceBody assistanceBody = new AssistanceBody(
                SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.CURRENT_DEVICE_ID, -1),
                SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.CURRENT_WORK_ZONE_ID, -1)
        );

        return retrofitInterface.registerAssistance(
                ApiClient.getAccessToken(),
                assistanceBody
        );
    }


}
