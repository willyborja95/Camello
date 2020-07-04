package com.apptec.camello.mainactivity.fdevice;

import com.apptec.camello.models.DeviceModel;
import com.apptec.camello.models.UpdatePushTokenBody;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    Call<GeneralResponse> getDeviceInfo(
            @Header(Constants.AUTHORIZATION_HEADER) String accessToken,
            @Path("imei") String IMEI);


    // We use Json Object because actually we don need that data response
    @PATCH(Constants.UPDATE_FIREBASE_TOKEN_URL)
    Call<GeneralResponse<JsonObject>> updateFirebaseToken(
            @Header(Constants.AUTHORIZATION_HEADER) String accessToken,
            @Path("id") int id,
            @Body UpdatePushTokenBody updatePushTokenBody
    );


}
