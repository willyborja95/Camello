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

public class ApiClient {
    /**
     * ApiClient provides a retrofit instance for make the call to the servere
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
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
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


    public static void saveAccessToken(String value){
        /**
        * Save the access token into shared preferences in private mode
        * */
        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, value);
    }

    public static String getRefreshToken(){
        /**
         * Get the refresh token previously save in shared preferences
         */
        return SharedPreferencesHelper.getStringValue(Constants.USER_REFRESH_TOKEN, "");
    }

    public static void saveRefreshToken(String value){
        /**
         * Save the refresh token into shared preferences in private mode
         */
        SharedPreferencesHelper.putStringValue(Constants.USER_REFRESH_TOKEN, value);
    }

    public static void askNewTokenWithRefreshToken(){
        /**
         * When the access token has expired. We request a new one with the refresh token.
         *
         * Ask for a new token.
         * Save it on shared preferences
         */

        // ToDo:


    }

    public static boolean isRefreshTokenExpired(){
        /**
         * Return true when the refresh token is already expired
         */
        // ToDo:
        return true;
    }

    public static boolean isAccessTokenExpired(){
        /**
         * Return true when the access token is already expired
         */
        // ToDo:
        return true;
    }


}
