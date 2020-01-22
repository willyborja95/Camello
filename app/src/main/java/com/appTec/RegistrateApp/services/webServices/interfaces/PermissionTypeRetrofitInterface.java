package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.appTec.RegistrateApp.models.Device;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PermissionTypeRetrofitInterface {

    @GET("permiso/")
    Call<JsonObject> get(@Header("authorization") String token);
}
