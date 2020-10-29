package com.apptec.camello;

import androidx.lifecycle.MutableLiveData;

import com.apptec.camello.mainactivity.BaseProcessListener;
import com.apptec.camello.mainactivity.fhome.AssistanceRetrofitInterface;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AssistanceStateTest {


    @Test
    public void testAssistanceStatus() {
        getAssistanceStatus(null, null);
    }


    public void getAssistanceStatus(@Nullable BaseProcessListener listener, MutableLiveData<Boolean> isWorking) {
        if (listener != null) {
            listener.onProcessing(null, null);
        }

        AssistanceRetrofitInterface retrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);

        Call<GeneralResponse<JsonObject>> call = retrofitInterface.getAssistanceState("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEyMywiZW1wbG95ZWVJZCI6NzgsInJvbGVJZCI6MCwic2NoZWR1bGVJZCI6NTYsImlhdCI6MTYwMzk3OTQzNCwiZXhwIjoxNjA0MDA4MjM0fQ.FNeGQs2oCYdAwHtcXGkUEkZsiw8y0TJKbveloUvWlgw");

        call.enqueue(new GeneralCallback<GeneralResponse<JsonObject>>(call) {
            @Override
            public void onFinalResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                // Get the assistance status
                if (response.code() == 200) {
                    Timber.d("" + response.body().getWrappedData().get("state"));
                    if (response.body().getWrappedData().get("state").equals("clockIn")) {
                        Timber.d("State working");
                    } else if (response.body().getWrappedData().get("state").equals("clockOut")) {
                        Timber.d("State not working");
                    }

                } else {
                    onFinalFailure(call, new Throwable("Response not successful"));
                }
            }
        });


    }


}