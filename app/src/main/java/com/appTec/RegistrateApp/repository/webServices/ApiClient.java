package com.appTec.RegistrateApp.services.webServices;

import android.content.Context;
import android.content.SharedPreferences;

import com.appTec.RegistrateApp.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    private static String token = null;

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
        /*
        * Get the token previously saved without context
        * */
        return ApiClient.token;
    }


    public static void setToken(String value, Context context){
        /*
        * Save the token into shared preferences in private mode
        * */
        SharedPreferences sharedPref = context.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_TOKEN, value);
        editor.commit();
    }

}