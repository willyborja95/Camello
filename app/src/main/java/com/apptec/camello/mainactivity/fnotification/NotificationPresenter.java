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
            listener.onProcessing(null, null);
        }

        NotificationsRetrofitInterface retrofitInterface =
                ApiClient.getClient().create(NotificationsRetrofitInterface.class);


        Call<GeneralResponse<List<NotificationModel>>> call = retrofitInterface.getAllNotifications(ApiClient.getAccessToken());

        call.enqueue(new GeneralCallback<GeneralResponse<List<NotificationModel>>>(call) {
            @Override
            public void onFinalResponse(Call<GeneralResponse<List<NotificationModel>>> call, Response<GeneralResponse<List<NotificationModel>>> response) {
                if (response.isSuccessful()) {
                    // Save the response into the database
                    if (response.body() != null && response.body().getWrappedData() != null && !response.body().getWrappedData().isEmpty()) {
                        RoomHelper.getAppDatabaseInstance().notificationDao().insertOrReplace(response.body().getWrappedData());
                    }

                    // Notify the listener that all was sync
                    if (listener != null) {
                        listener.onSuccessProcess(null, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onErrorOccurred(R.string.error, R.string.unknown_error);
                    }
                }



            }

            /**
             * Method to be override by the calling class

             */
            @Override
            public void onFinalFailure(Call<GeneralResponse<List<NotificationModel>>> call, Throwable t) {

                if (listener != null) {
                    listener.onErrorOccurred(R.string.no_internet_connection_title, R.string.no_internet_connection);
                }
            }
        });


    }

}




