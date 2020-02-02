package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.appTec.RegistrateApp.models.Assistance;
import com.appTec.RegistrateApp.models.Device;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AssistanceRetrofitInterface {
    @POST("asistencia/")
    Call<JsonObject> post(@Header("authorization") String token, @Body Assistance assistance);

    @GET("asistencia")
    Call<JsonObject> get(@Header("authorization") String token, @Query("date") String date);
}
