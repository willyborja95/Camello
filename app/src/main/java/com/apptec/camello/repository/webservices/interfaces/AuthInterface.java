package com.apptec.camello.repository.webservices.interfaces;

import com.apptec.camello.auth.refreshtoken.RefreshTokenBody;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Auth Interface
 */

public interface AuthInterface {

    @POST(Constants.REFRESH_TOKEN_URL)
    Call<GeneralResponse<JsonObject>> refreshToken(
            @Body RefreshTokenBody refreshTokenBody
    );

}
