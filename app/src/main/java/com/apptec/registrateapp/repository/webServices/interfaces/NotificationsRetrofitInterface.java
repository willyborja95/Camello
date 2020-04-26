package com.apptec.registrateapp.repository.webServices.interfaces;

import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NotificationsRetrofitInterface {

    // Get all the notifications
    @GET(Constants.NOTIFICATIONS_URL)
    Call<JsonObject> get(@Header("authorization") String token);
}
