package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface NotificationsRetrofitInterface {

    // Get all the notifications
    @GET(Constants.NOTIFICATIONS_URL)
    Call<JsonObject> get(@Header("authorization") String token);
}
