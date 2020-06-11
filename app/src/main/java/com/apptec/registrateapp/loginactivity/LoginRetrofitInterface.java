package com.apptec.registrateapp.loginactivity;

import com.apptec.registrateapp.models.UserCredential;
import com.apptec.registrateapp.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.registrateapp.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface LoginRetrofitInterface {


    @Headers(value = {"Accept: application/json"})
    @POST(Constants.LOGIN_URL)
    Call<GeneralResponse<LoginDataResponse>> login(@Body UserCredential credential);


//    @POST("login/refresh")
//    Call<JsonObject> login(@Body refreshToken);

}
