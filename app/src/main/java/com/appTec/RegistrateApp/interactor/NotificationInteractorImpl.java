package com.appTec.RegistrateApp.interactor;

import android.app.Activity;
import android.content.Context;

import com.appTec.RegistrateApp.App;
import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.presenter.NotificationPresenterImpl;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.NotificationsRetrofitInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationInteractorImpl implements NotificationInteractor {
    /*
     * This class will get for the presenter all the data requested
     * */

    // Attributes
    private NotificationPresenterImpl notificationPresenter;
    private boolean canceled;

    public NotificationInteractorImpl(NotificationPresenterImpl notificationPresenter) {
        /*
         * Constructor
         * */

        this.notificationPresenter = notificationPresenter;
    }

    @Override
    public void getNotifications() {
        /*
         * Here goes the interaction with the source of data. In this case the source is a web service
         * */
        NotificationsRetrofitInterface notificationsRetrofitInterface = ApiClient.getClient().create(NotificationsRetrofitInterface.class);
        Call<JsonObject> notificationCall = notificationsRetrofitInterface.get(ApiClient.getToken(App.getContext()));

        notificationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                ArrayList<Notification> notificationsResult = new ArrayList<Notification>();        // Result array that will be sent to the Presenter

                JsonArray notificationListJson = new JsonArray(); //response.body().getAsJsonArray("data");

                if (notificationListJson.size() == 0) {
                    /*
                     * Todo
                     * */
                    if (notificationPresenter != null) {
                        notificationPresenter.showNotNewNotificationsMessage();
                    }
                } else {
                    /*
                     * Parse the data into notifications objects
                     * */


                    for (int i = 0; i < notificationListJson.size(); i++) {


                        JsonObject notificationJson = notificationListJson.get(i).getAsJsonObject();

                        // Todo: Parse the data
//                        String title = notificationJson.get("title").getAsString();
//                        String sentDate = notificationJson.get("sentDate").getAsString();
//
//
//
//                        Notification notification = new Notification();                             // Create the object notification
//
//                        notificationsResult.add(notification);                                             // Add it to the array result
                    }

                    // Sent the result array to the Presenter
                    if (!canceled) {
                        notificationPresenter.showNotifications(notificationsResult);
                    } else {
                        notificationCall.cancel();
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                /*
                 * Todo
                 * */
                if (notificationCall != null) {
                    notificationCall.cancel();
                }
            }
        });


    }

    @Override
    public void detachJob() {
        canceled = true;
        notificationPresenter = null;
    }


}
