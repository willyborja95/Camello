package com.apptec.registrateapp.mainactivity.fhome;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.apptec.registrateapp.repository.webservices.GeneralCallback;
import com.apptec.registrateapp.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * This class will is a Runnable that handles the working status change
 */
public class HandlerChangeWorkingStatus implements Runnable {

    // This instance of WorkZoneModel will be useful when the transition will be an enter
    private WorkZoneModel workZoneModel;

    /**
     * Constructor called when the transition will be an enter
     *
     * @param workZoneModel
     */
    public HandlerChangeWorkingStatus(WorkZoneModel workZoneModel) {
        this.workZoneModel = workZoneModel;
    }

    /**
     * Empty constructor when we don't know which is the work zone
     */
    public HandlerChangeWorkingStatus() {
    }

    @Override
    public void run() {

        /**
         * Change the working status
         */
        Timber.d("Init change working status");


        if (RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod() == null) {
            /** We entry here when is the first time when the button is pressed since the app was installed.*/
            Timber.d("Not worker initialized");
            Timber.i("New worker created");

            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);
            App.getGeofenceHelper().startTracking(workZoneModel);
            notifyTheServer(workZoneModel);
        } else if (isWorking()) {
            /** If the user is working now, then finish whe work and create a new one */
            Timber.i("Finishing job and creating a new one");
            RoomHelper.getAppDatabaseInstance().workingPeriodDao().changeLastWorkingPeriod(Constants.INT_FINISHED_STATUS);
            createAndSaveWorkingPeriod(Constants.INT_NOT_INIT_STATUS);
            App.getGeofenceHelper().stopTracking();
            notifyTheServer(null);
        } else if (isNotInitWorking()) {
            /** If the period of working is not init start it*/
            Timber.i("A new working period created");
            createAndSaveWorkingPeriod(Constants.INT_WORKING_STATUS);
            App.getGeofenceHelper().startTracking(workZoneModel);
            notifyTheServer(workZoneModel);
        }

    }


    /**
     * Notify the server
     *
     * @param workZoneModel When the param is -1 means an exit
     */
    private void notifyTheServer(@Nullable WorkZoneModel workZoneModel) {
        AssistanceRetrofitInterface retrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Call<GeneralResponse<JsonObject>> call;
        if (workZoneModel == null) { // We don't have the work zone because the event is an exit
            call = getCall(retrofitInterface);
        } else {
            call = getCall(retrofitInterface, workZoneModel);
        }


        call.enqueue(new GeneralCallback<GeneralResponse<JsonObject>>(call) {
            @Override
            public void onResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                Timber.i("Assistance changed");
                Timber.i("Request code: " + response.code());
            }
        });

    }

    private Call<GeneralResponse<JsonObject>> getCall(AssistanceRetrofitInterface retrofitInterface, WorkZoneModel workZoneModel) {
        Call<GeneralResponse<JsonObject>> call = retrofitInterface.registerAssistance(
                ApiClient.getAccessToken(),
                workZoneModel
        );
        return call;
    }

    private Call<GeneralResponse<JsonObject>> getCall(AssistanceRetrofitInterface retrofitInterface) {
        Call<GeneralResponse<JsonObject>> call = retrofitInterface.registerAssistance(
                ApiClient.getAccessToken()
        );
        return call;
    }


    /**
     * Return true when the last working status was 'working'
     *
     * @return
     */
    private boolean isWorking() {

        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_WORKING_STATUS;
    }

    /**
     * Return true when the last working status was 'not init'
     *
     * @return
     */
    private boolean isNotInitWorking() {
        return RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().getStatus() == Constants.INT_NOT_INIT_STATUS;
    }

    /**
     * Create a new working period with
     * the param
     *
     * @param status For example could be: 'not init'
     */
    private void createAndSaveWorkingPeriod(int status) {
        WorkingPeriodModel startedWorkingPeriod = new WorkingPeriodModel();
        startedWorkingPeriod.setStatus(status);
        RoomHelper.getAppDatabaseInstance().workingPeriodDao().insert(startedWorkingPeriod);
    }


}
