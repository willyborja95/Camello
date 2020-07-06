package com.apptec.camello.mainactivity.fhome;

import com.apptec.camello.App;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
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

            startWorking(workZoneModel, true);

        } else if (isWorking()) {
            /** If the user is working now, then finish whe work and create a new one */

            stopWorking();

        } else if (isNotInitWorking()) {
            /** If the period of working is not init start it*/
            startWorking(workZoneModel, false);
        }

        Timber.i(RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod().toString());
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
                FirebaseCrashlytics.getInstance().recordException(new Exception("Test exception"));
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
     * Create a new working period into database and set the start working time to the current time
     */
    private void startWorking(WorkZoneModel workZoneModel, boolean firstTime) {
        Timber.i("A new working period created");

        if (firstTime) {
            // If there is not a previous working period into the database
            Timber.d("Not previous working period created, maybe the app is running by first time");

            Timber.d("Created a new working period");
            // Create a working period instance
            WorkingPeriodModel startedWorkingPeriod = new WorkingPeriodModel(
                    System.currentTimeMillis(),   // Current time
                    Constants.INT_WORKING_STATUS, // Not init status
                    workZoneModel.getId());       // Foreign key to the work zone

            Timber.d("Save the working period into database");
            // Save the working period into database
            RoomHelper.getAppDatabaseInstance().workingPeriodDao().insert(startedWorkingPeriod);

        } else {
            // Change the status of the last working period to "working"

            new Thread(() -> {
                Timber.d("Change the status of the last working period to \"working\"");
                // Get the last working period
                WorkingPeriodModel lastWorkingPeriod = RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLastWorkingPeriod();

                // Update it
                lastWorkingPeriod.setStatus(Constants.INT_WORKING_STATUS);
                lastWorkingPeriod.setStart_date(System.currentTimeMillis());
                lastWorkingPeriod.setWorkZoneId(workZoneModel.getId());

                // Save the changes into database
                RoomHelper.getAppDatabaseInstance().workingPeriodDao().updateWorkingPeriod(lastWorkingPeriod);

            }).start();

        }


        // Start tracking the work zone
        App.getGeofenceHelper().startTracking(workZoneModel);

        // Notify the server
        notifyTheServer(workZoneModel);
    }

    /**
     * Change the last working period on the database and set the
     */
    private void stopWorking() {
        Timber.i("Finishing job and creating a new one");

        // Change the status of the last working period to "finished" and the end time
        // to the current time
        new Thread(() -> {
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

        }).start();

        // Stop tracking the work zone
        App.getGeofenceHelper().stopTracking();

        // Notify the server
        notifyTheServer(null);
    }





}
