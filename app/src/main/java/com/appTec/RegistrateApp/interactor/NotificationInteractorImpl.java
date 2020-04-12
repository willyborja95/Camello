package com.appTec.RegistrateApp.interactor;

import android.content.Context;
import android.content.SharedPreferences;

import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.presenter.NotificationPresenter;
import com.appTec.RegistrateApp.presenter.NotificationPresenterImpl;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.NotificationsRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
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
        Call<JsonObject> notificationCall = notificationsRetrofitInterface.get(getUserToken());

        notificationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                ArrayList<Notification> notificationsResult = new ArrayList<Notification>();        // Result array that will be sent to the Presenter

                JsonArray notificationListJson = response.body().getAsJsonArray("data");

                if (notificationListJson.size() == 0) {
                    /*
                    * Todo
                    * */

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
                    notificationPresenter.showNotifications(notificationsResult);
                }





            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                /*
                * Todo
                * */
            }
        });


    }

    @Override
    public void showNotifications(ArrayList<Notification> notifications) {
        notificationPresenter.showNotifications(notifications);
    }


}
