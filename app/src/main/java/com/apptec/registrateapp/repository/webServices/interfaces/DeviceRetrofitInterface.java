package com.apptec.registrateapp.repository.webServices.interfaces;

import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceRetrofitInterface {


    @POST("dispositivo/")
    Call<JsonObject> post(@Header("authorization") String token, @Body Device device);

    @GET("dispositivo/empleado/{userId}")
    Call<JsonObject> get(@Header("authorization") String token, @Path("userId") int userId);


    /**
     * Permite consultar si el dispositivo actual del empleado.
     *
     * Se debe especificar el identificador del dispositivo.
     *
     * https://registrateapp-staging/api/employees/device?identifier=IMEI
     * HEADERS
     * Authorization
     * PARAMS
     * identifier IMEI
     */
    @GET("/api/employee/device?identifier={indentifier}")
    Call<JsonObject> getDeviceInfo(
            @Header(Constants.AUTHORIZATION_HEADER) String accessToken,
            @Path("identifier") String IMEI);
}
