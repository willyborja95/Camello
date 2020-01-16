package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.appTec.RegistrateApp.models.UserCredential;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginRetrofitInterface {
    @Headers({"Accept: application/json"})
    @POST("login/")
    Call<JsonObject> login(@Body UserCredential credential );
}
