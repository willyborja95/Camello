package com.appTec.RegistrateApp.repository.webServices;

import android.content.Context;
import android.content.SharedPreferences;

import com.appTec.RegistrateApp.repository.sharedpreferences.SharedPreferencesHelper;
import com.appTec.RegistrateApp.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



    public static String getToken(){
        /**
        * Get the token previously saved without context
        * */
        return SharedPreferencesHelper.getStringValue(Constants.USER_ACCESS_TOKEN, "");
    }


    public static void setToken(String value){
        /**
        * Save the token into shared preferences in private mode
        * */
        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, value);
    }

}
