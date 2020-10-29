package com.apptec.camello.mainactivity.fhome.geofence;

import com.apptec.camello.App;
import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.mainactivity.fhome.AssistanceBody;
import com.apptec.camello.mainactivity.fhome.AssistanceRetrofitInterface;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.apptec.camello.util.ErrorDictionary;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Class runnable that will start all thin g to set the user working
 */
public class StartWorking<T extends BaseProcessListener> implements Runnable {

    // Attributes
    private WorkZoneModel workZoneModel;


    // Listener
    private T listener;


    /**
     * Ideal constructor
     *
     * @param workZoneModel work zone when the user will going to work
     * @param listener      listener of the process
     */
    public StartWorking(WorkZoneModel workZoneModel, T listener) {
        this.workZoneModel = workZoneModel;
        this.listener = listener;
    }


    @Override
    public void run() {

        // Indicates the listener that we take care of the process now
        listener.onProcessing(null, null);

        Timber.d("Handling the request to start working");

        // Notify the server first
        notifyTheServer(workZoneModel);


    }


    /**
     * Notify the server
     *
     * @param workZoneModel When the param is -1 means an exit
     */
    private void notifyTheServer(@Nullable WorkZoneModel workZoneModel) {
        Timber.d("Notify the server the user has change his working status");
        AssistanceRetrofitInterface retrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Call<GeneralResponse<JsonObject>> call;

        call = getCall(retrofitInterface, workZoneModel);


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

                if (response.code() >= 200 && response.code() < 300) {

                    if (response.body().isOk()) {
                        Timber.i("Assistance changed");

                        Timber.i("Request code: %s", response.code());

                        SharedPreferencesHelper.putIntValue(Constants.CURRENT_WORK_ZONE_ID, workZoneModel.getId());
                        changeWorkingStatus();

                        // Notify the listener
                        listener.onSuccessProcess(R.string.entrance_has_been_registered, null);
                        return;
                    }
                }
                try {
                    int errorCode = response.body().getError().getCode();
                    int message = ErrorDictionary.getErrorMessageByCode(errorCode);
                    listener.onErrorOccurred(R.string.error, message);
                } catch (Exception e) {
                    listener.onErrorOccurred(R.string.error, R.string.unknown_error);
                    Timber.e("Unknown error happened while start working");
                    FirebaseCrashlytics.getInstance().recordException(e);
                }


            }

            /**
             * Let know the listener that an error happen
             */
            @Override
            public void onFinalFailure(Call<GeneralResponse<JsonObject>> call, Throwable t) {
                listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
            }
        });

    }

    /**
     * This method change the working status into database
     */
    private void changeWorkingStatus() {

        // Start tracking the work zone
        App.getGeofenceHelper().startTracking(workZoneModel);
    }

    private Call<GeneralResponse<JsonObject>> getCall(AssistanceRetrofitInterface retrofitInterface, WorkZoneModel workZoneModel) {

        // Generate the body
        AssistanceBody assistanceBody = new AssistanceBody(
                SharedPreferencesHelper.getSharedPreferencesInstance().getInt(Constants.CURRENT_DEVICE_ID, 0),
                workZoneModel.getId()
        );

        Call<GeneralResponse<JsonObject>> call = retrofitInterface.registerAssistance(
                ApiClient.getAccessToken(),
                assistanceBody

        );

        Timber.w("Start working body: %s", assistanceBody);

        return call;
    }

}
