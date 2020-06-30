package com.apptec.registrateapp.mainactivity.fhome;

import com.apptec.registrateapp.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Interface with endpoint related to register the assistance
 */
public interface AssistanceRetrofitInterface {


    @POST(Constants.REGISTER_ASSISTANCE_URL)
    Call<GeneralResponse<JsonObject>> registerAssistance(
            @Header("authorization") String token,
            int workZoneId
    );

}
