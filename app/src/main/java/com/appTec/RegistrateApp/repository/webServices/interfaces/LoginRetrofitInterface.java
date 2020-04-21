package com.appTec.RegistrateApp.repository.webServices.interfaces;

import com.appTec.RegistrateApp.models.UserCredential;
import com.appTec.RegistrateApp.repository.webServices.pojoresponse.LoginResponse;
import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonObject;

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
