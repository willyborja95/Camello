package com.apptec.registrateapp.repository.webServices.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;


public interface AuthInterface {
    /**
     * Auth Interface
     */

    @POST("/api/auth/token")
    Call<JsonObject> refreshToken(
            @Field("accessToken") String accessToken,
            @Field("refreshToken") String refreshToken
    );

}
