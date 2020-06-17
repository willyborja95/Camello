package com.apptec.registrateapp.repository.webservices;

import com.apptec.registrateapp.auth.refreshtoken.RefreshTokenBody;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.repository.webservices.interfaces.AuthInterface;
import com.apptec.registrateapp.util.Constants;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import timber.log.Timber;

/**
 * This class will intercept the responses
 * <p>
 * This will intercept when the server return some response, but if there is not
 * the GeneralResponse class is handling it
 */
public class InterceptorImpl implements okhttp3.Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Timber.d("Interceptor cached the request");
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);


        // Deal with the issues the way we need to
        if (response.code() == 401) {
            Timber.e("Unauthorized");
            Timber.e("Response code is: " + 401);
            // Unauthorized
            // Ask a new refresh token and retry the call

            String newToken = askNewToken();

            // Retry call
            Timber.d("Retrying call");
            // use the original request with the new auth token
            Request newRequest = request.newBuilder().header(Constants.AUTHORIZATION_HEADER, newToken).build();
            Timber.d("Response before retry" + response.toString());
            response = chain.proceed(newRequest);
            Timber.d("Response after retry" + response.toString());
        }


        // otherwise just pass the original response on
        return response;


    }


    private String askNewToken() throws IOException {
        Timber.i("Asking a new token");
        AuthInterface authInterface = ApiClient.getClient().create(AuthInterface.class);
        Call<JsonObject> refreshCall = authInterface.refreshToken(
                new RefreshTokenBody(ApiClient.getAccessToken(), ApiClient.getRefreshToken())
        );


        // With do not enqueue the call because it is no necessary an immediate response from this worker
        retrofit2.Response<JsonObject> response = refreshCall.execute();
        // Get the new access token
        String newAccessToken = response.body().get("data").getAsJsonObject().get("accessToken").getAsString();
        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, newAccessToken); // Storage in shared preferences

        Timber.i("Token refreshed");
        Timber.d("New token: " + newAccessToken);

        return newAccessToken;
    }
}
