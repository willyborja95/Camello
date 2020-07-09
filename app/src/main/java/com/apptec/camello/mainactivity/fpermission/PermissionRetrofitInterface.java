package com.apptec.camello.mainactivity.fpermission;

import com.apptec.camello.models.PermissionStatus;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
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
    Call<GeneralResponse<PermissionDto>> createPermission(
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

    // api/permission/employee/3
    @GET(Constants.ALL_PERMISSIONS_URL)
    Call<GeneralResponse<List<PermissionDto>>> getAllPermissions(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Path("userId") int userId

    );

    /**
     * Endpoint to delete a permission by the id
     */
    @DELETE(Constants.PERMISSION_DELETE_URL)
    Call<JsonObject> deletePermission(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Path("permissionId") int permissionId
    );


}
