package com.apptec.camello.mainactivity.fhome;

import com.apptec.camello.mainactivity.fhome.geofence.SyncAssistanceBody;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

/**
 * Interface with endpoint related to register the assistance
 */
public interface AssistanceRetrofitInterface {


    /**
     * Endpoint used to register an enter
     *
     * @param token          access token
     * @param assistanceBody body with the workzoneId and the deviceID
     * @return Call<GeneralResponse < JsonObject>>
     */
    @POST(Constants.REGISTER_ASSISTANCE_URL)
    Call<GeneralResponse<JsonObject>> registerAssistance(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Body AssistanceBody assistanceBody
    );


    /**
     * Endpoint to sync the last time when the user exit
     *
     * @param token access token
     * @param body  Body with the last time when the user was inside the work zone
     * @return Call<GeneralResponse < JsonObject>>
     */
    @PATCH(Constants.SYNC_ASSISTANCE_URL)
    Call<GeneralResponse<JsonObject>> syncAssistance(
            @Header(Constants.AUTHORIZATION_HEADER) String token,
            @Body SyncAssistanceBody body
    );


}
