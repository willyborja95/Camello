package com.apptec.registrateapp.repository.webServices.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PermissionRetrofitInterface {
    @POST("permiso/")
    Call<JsonObject> post(@Header("authorization") String token, @Body JsonObject permission);

    @GET("permiso/empleado/{userId}")
    Call<JsonObject> get(@Header("authorization") String token, @Path("userId") int userId);

    @DELETE("permiso/{permisoId}")
    Call<JsonObject> delete(@Header("authorization") String token, @Path("permisoId") int permisoId);
}
