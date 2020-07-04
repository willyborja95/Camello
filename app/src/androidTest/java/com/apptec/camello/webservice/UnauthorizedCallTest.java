package com.apptec.camello.webservice;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.apptec.camello.mainactivity.fpermission.PermissionRetrofitInterface;
import com.apptec.camello.models.PermissionStatus;
import com.apptec.camello.repository.webservices.ApiClient;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

@RunWith(AndroidJUnit4.class)
public class UnauthorizedCallTest {


    PermissionRetrofitInterface permissionRetrofitInterface;

    Retrofit retrofit;

    /**
     *
     */
    @Before
    public void buildService() {
        retrofit = ApiClient.getClient();
        permissionRetrofitInterface =
                ApiClient.getClient().create(PermissionRetrofitInterface.class);
        Timber.w("---------------------------------------------");
        Timber.w("Running the test");
    }


    /**
     * Here we test how the app handle when the server return a unauthorized response
     */
    @Test
    public void testUnauthorizedRequest() {

        // Test to bring the permission status catalog
        // 2. Pull the permission status catalog
        Timber.d("Running test: testUnauthorizedRequest");
        Timber.d("Starting to pull the permission status catalog available");


        // Pull the permission status
        Call<GeneralResponse<List<PermissionStatus>>> callStatus = permissionRetrofitInterface.getPermissionStatusWrapped("invalid token (test)");

        try {
            // With do not enqueue the call because it is necessary an immediate response
            Response<GeneralResponse<List<PermissionStatus>>> response = callStatus.execute();


            Timber.d("Permission status call executed");
            Timber.d("Here is the final response body: " + response.toString()


            );


        } catch (IOException e) {
            Timber.e("Error");
            Timber.e(e);

        }


    }


    @After
    public void after() {
        Timber.w("Test finished");
        Timber.w("--------------------------------------------");
    }
}
