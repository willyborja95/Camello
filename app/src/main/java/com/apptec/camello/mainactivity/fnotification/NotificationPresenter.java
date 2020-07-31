package com.apptec.camello.mainactivity.fnotification;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.models.NotificationModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * This class is in the middle of the MVVM pattern.
 * It interacts as an intermediary between the Model and the View.
 */
public class NotificationPresenter {


    /**
     * Empty constructor
     */
    public NotificationPresenter() {
    }

    /**
     * Return the notifications into the live data
     */
    public LiveData<List<NotificationModel>> loadNotificationsLiveData() {
        return RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
    }

    /**
     * Method for sync the notifications with the server
     */
    public void syncNotifications(@Nullable BaseProcessListener listener) {
        if (listener != null) {
            listener.onProcessing();
        }

        NotificationsRetrofitInterface retrofitInterface =
                ApiClient.getClient().create(NotificationsRetrofitInterface.class);

        Call<GeneralResponse> call = retrofitInterface.getAllNotifications(ApiClient.getAccessToken());

        call.enqueue(new GeneralCallback<GeneralResponse>(call) {
            @Override
            public void onFinalResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                // Save the response into the database
                // TODO

                // Notify the listener that all was sync
                if (listener != null) {
                    listener.onSuccessProcess();
                }

            }

            /**
             * Method to be override by the calling class
             *
             * @param call call
             * @param t    throwable
             */
            @Override
            public void onFinalFailure(Call<GeneralResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
                }
            }
        });


    }

}
