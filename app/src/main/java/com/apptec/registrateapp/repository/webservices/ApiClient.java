package com.apptec.registrateapp.repository.webservices;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ApiClient {
    /**
     * ApiClient provides a retrofit instance for make the call to the server
     */

    private static Retrofit retrofit = null;


    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);


            // Deal with the issues the way we need to
            if (response.code() == 401) {
                // Unauthorized
                // TODO: Ask a new refresh token

            }


            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < 3) {

                Timber.d("Request is not successful" + tryCount);

                tryCount++;

                // retry the request
                response = chain.proceed(request);
            }

            // otherwise just pass the original response on
            return response;


        }
    };

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().create();
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }



    public static String getAccessToken(){
        /**
        * Get the access token previously saved into shared preferences
        * */
        return SharedPreferencesHelper.getStringValue(Constants.USER_ACCESS_TOKEN, "");
    }


    public static String getRefreshToken(){
        /**
         * Get the refresh token previously save in shared preferences
         */
        return SharedPreferencesHelper.getStringValue(Constants.USER_REFRESH_TOKEN, "");
    }





}
