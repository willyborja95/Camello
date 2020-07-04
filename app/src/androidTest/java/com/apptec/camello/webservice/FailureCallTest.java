package com.apptec.camello.webservice;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.GeneralCallback;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.google.gson.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;


/**
 * To see the result of this test, you should disconnect the used device from the network.
 * Turn off the wifi and mobile data
 */
@RunWith(AndroidJUnit4.class)
public class FailureCallTest {


    TestInterface testInterface;
    Retrofit retrofit;


    // Signal for wait the async all
    final CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void buildService() {
        retrofit = ApiClient.getClient();
        testInterface = ApiClient.getClient().create(TestInterface.class);
        Timber.w("---------------------------------------------");
        Timber.w("Running the test");
    }


    /**
     * This test is for see the General Call back in action when a failure occur doing the call
     * For example if you don't have internet or the endpoint does not match with the server
     */
    @Test
    public void testOnFailureCall() throws InterruptedException {
        Timber.d("Running test: testOnFailureCall");

        // We try here to do a call to a missing endpoint of the server


        Timber.d("Making an async call to a missing endpoint");


        // Pull the permission status
        Call<GeneralResponse<JsonObject>> call = testInterface.get(ApiClient.getAccessToken());


        call.enqueue(new GeneralCallback<GeneralResponse<JsonObject>>(call) {
            @Override
            public void onResponse(Call<GeneralResponse<JsonObject>> call, Response<GeneralResponse<JsonObject>> response) {
                // With do not enqueue the call because is necessary an immediate response

                Timber.d("Call executed");
                Timber.d("Here is the final response body: " + response.toString());


                signal.countDown();// notify the count down latch

            }
        });


        signal.await();// wait for callback

    }


    @After
    public void after() {
        Timber.w("Test finished");
        Timber.w("--------------------------------------------");
    }
}
