package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.appTec.RegistrateApp.models.Device;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeviceRetrofitInterface {
    @Headers({"Accept: application/json"})
    @POST("dispositivo/")
    Call<JsonObject> post(@Header ("Authentication") String token, @Body Device device );
}
