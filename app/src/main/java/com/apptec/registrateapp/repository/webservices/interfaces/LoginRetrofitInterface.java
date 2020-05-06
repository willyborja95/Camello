package com.apptec.registrateapp.repository.webservices.interfaces;

import com.apptec.registrateapp.models.UserCredential;
import com.apptec.registrateapp.repository.webservices.pojoresponse.loginresponse.LoginResponse;
import com.apptec.registrateapp.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginRetrofitInterface {
    @Headers({"Accept: application/json"})
    @POST(Constants.LOGIN_URL)
    Call<LoginResponse> login(@Body UserCredential credential);

//    @POST("login/refresh")
//    Call<JsonObject> login(@Body refreshToken);

}
