package com.appTec.RegistrateApp.repository.webServices.interfaces;

import com.appTec.RegistrateApp.models.Device;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceRetrofitInterface {
    @POST("dispositivo/")
    Call<JsonObject> post(@Header("authorization") String token, @Body Device device);

    @GET("dispositivo/empleado/{userId}")
    Call<JsonObject> get(@Header("authorization") String token, @Path("userId") int userId);
}
