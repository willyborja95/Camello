package com.apptec.camello.webservice;

import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TestInterface {

    @GET("fail/endpoint")
    Call<GeneralResponse<JsonObject>> get(@Header("authorization") String token);


}
