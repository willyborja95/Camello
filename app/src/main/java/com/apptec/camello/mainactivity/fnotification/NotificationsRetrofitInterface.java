package com.apptec.camello.mainactivity.fnotification;

import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NotificationsRetrofitInterface {


    /**
     * Get all the notifications
     *
     * @param token access token
     * @return General response
     */
    @GET(Constants.NOTIFICATIONS_URL)
    Call<GeneralResponse> getAllNotifications(@Header("authorization") String token);


}
