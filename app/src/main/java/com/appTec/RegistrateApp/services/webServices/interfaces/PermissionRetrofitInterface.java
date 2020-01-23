package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PermissionRetrofitInterface {
    @POST("permiso/")
    Call<JsonObject> post(@Header("authorization") String token, @Body JsonObject permission);
}
