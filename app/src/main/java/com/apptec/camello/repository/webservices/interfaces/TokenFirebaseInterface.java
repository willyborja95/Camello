package com.apptec.camello.repository.webservices.interfaces;

import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenFirebaseInterface {

    /**
     * This class will end the token of the to the server to control the notifications
     * @param access_token
     * @return
     */
    @POST(Constants.ENDPOINT_FIREBASE)
    Call<JsonObject> post(@Header(Constants.AUTHORIZATION_HEADER) String access_token, @Body String firebase_token);

}
