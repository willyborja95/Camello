package com.apptec.registrateapp.repository.webServices;

import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    /**
     * ApiClient provides a retrofit instance for make the call to the servere
     */

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().create();
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
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
