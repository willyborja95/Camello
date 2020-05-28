package com.apptec.registrateapp.mainactivity.fpermission;

import com.apptec.registrateapp.models.PermissionStatus;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PermissionRetrofitInterface {


    @Deprecated
    @POST("permiso/")
    Call<JsonObject> post(@Header("authorization") String token, @Body JsonObject permission);

    @Deprecated
    @GET("permiso/empleado/{userId}")
    Call<JsonObject> get(@Header("authorization") String token, @Path("userId") int userId);

    @Deprecated
    @DELETE("permiso/{permisoId}")
    Call<JsonObject> delete(@Header("authorization") String token, @Path("permisoId") int permisoId);

    @POST(Constants.PERMISSION_CREATE_URL)
    Call<JsonObject> createPermission(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Body PermissionDto permission);


    @GET(Constants.PERMISSION_TYPES_CATALOG_URL)
    Call<GeneralResponse<List<PermissionType>>> getPermissionTypesWrapped(
            @Header(Constants.AUTHORIZATION_HEADER) String token
    );

    @GET(Constants.PERMISSION_STATUS_CATALOG_URL)
    Call<GeneralResponse<List<PermissionStatus>>> getPermissionStatusWrapped(
            @Header(Constants.AUTHORIZATION_HEADER) String token
    );





    @GET(Constants.PERMISSIONS_STATUS_URL)
    Call<JsonObject> getPermissionStatus(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Path("userId") int userId
    );

    @GET(Constants.ALL_PERMISSIONS_URL)
        // api/permission/employee/3
    Call<List<PermissionDto>> getAllPermissions(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Path("userId") int userId

    );


}
