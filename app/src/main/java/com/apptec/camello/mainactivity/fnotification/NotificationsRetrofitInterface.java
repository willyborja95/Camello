package com.apptec.camello.mainactivity.fnotification;

import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NotificationsRetrofitInterface {

    // Get all the notifications
    // This is not used for now because we do not have a endpoint that provide us
    // the notifications sent.
    @GET(Constants.NOTIFICATIONS_URL)
    Call<JsonObject> get(@Header("authorization") String token);
}
