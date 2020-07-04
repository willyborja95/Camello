package com.apptec.camello.repository.webservices.interfaces;

import com.apptec.camello.auth.refreshtoken.RefreshTokenBody;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AuthInterface {
    /**
     * Auth Interface
     */

    @POST(Constants.REFRESH_TOKEN_URL)
    Call<JsonObject> refreshToken(
            @Body RefreshTokenBody refreshTokenBody
    );

}
