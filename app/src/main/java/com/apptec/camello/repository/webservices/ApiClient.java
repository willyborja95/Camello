package com.apptec.camello.repository.webservices;

import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    /**
     * @Override public Response intercept(Chain chain) throws IOException {
     * return null;
     * }* ApiClient provides a retrofit instance for make the call to the server
     */

    private static Retrofit retrofit = null;


    private static Interceptor interceptor = new InterceptorImpl();

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
