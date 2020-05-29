package com.apptec.registrateapp.mainactivity.fdevice;

import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.UpdatePushTokenBody;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeviceRetrofitInterface {


    @POST(Constants.REGISTER_DEVICE_URL)
    Call<JsonObject> registerDevice(@Header(Constants.AUTHORIZATION_HEADER) String token, @Body DeviceModel device);

    @GET("dispositivo/empleado/{userId}")
    Call<JsonObject> get(@Header(Constants.AUTHORIZATION_HEADER) String token, @Path("userId") int userId);


    /**
     * HEADERS
     * Authorization
     * PARAMS
     * identifier IMEI
     */
    @GET(Constants.REQUEST_DEVICE_INFO_URL)
    Call<JsonObject> getDeviceInfo(
            @Header(Constants.AUTHORIZATION_HEADER) String accessToken,
            @Query("identifier") String IMEI);


    @PATCH(Constants.UPDATE_FIREBASE_TOKEN_URL)
    Call<JsonObject> updateFirebaseToken(
            @Header(Constants.AUTHORIZATION_HEADER) String accessToken,
            @Path("id") int id,
            @Body UpdatePushTokenBody updatePushTokenBody
    );



}
