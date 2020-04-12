package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TimeRetrofit {
    @GET("asistencia/time")
    Call<JsonObject> get(@Header("authorization") String token);
}
