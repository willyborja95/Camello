package com.appTec.RegistrateApp.services.webServices.interfaces;

import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PermissionRetrofitInterface {
    @POST("permiso/")
    Call<JsonObject> post(@Header("authorization") String token, @Body JsonObject permission );
}
