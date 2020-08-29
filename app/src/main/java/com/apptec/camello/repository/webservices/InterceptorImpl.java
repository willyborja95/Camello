package com.apptec.camello.repository.webservices;

import com.apptec.camello.auth.refreshtoken.RefreshTokenBody;
import com.apptec.camello.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.camello.repository.webservices.interfaces.AuthInterface;
import com.apptec.camello.repository.webservices.pojoresponse.GeneralResponse;
import com.apptec.camello.util.Constants;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
        if (isTokenExpired(response)) {
            Timber.e("Token expired");

            // Unauthorized
            // Ask a new refresh token and retry the call

            try {
                String newToken = askNewToken();
                // Retry call
                Timber.d("Retrying call");
                // use the original request with the new auth token
                Request newRequest = request.newBuilder().header(Constants.AUTHORIZATION_HEADER, newToken).build();
                Timber.d("Response before retry%s", response.toString());
                response = chain.proceed(newRequest);
                Timber.d("Response after retry%s", response.toString());
            } catch (NullPointerException npe) {
                Timber.e("Probably a invalid token is being sent");
                // TODO: Logout the user
                Timber.w("We should logout the user here");

            }


        }


        // otherwise just pass the original response on
        return response;


    }


    private String askNewToken() throws IOException, NullPointerException {
        Timber.i("Asking a new token");
        AuthInterface authInterface = ApiClient.getClient().create(AuthInterface.class);
        String refreshToken = ApiClient.getRefreshToken();

        Call<GeneralResponse<JsonObject>> refreshCall = authInterface.refreshToken(
                new RefreshTokenBody(ApiClient.getAccessToken(), refreshToken)
        );
        Timber.d("Refresh token: %s", refreshToken);

        // With do not enqueue the call because it is no necessary an immediate response from this worker
        retrofit2.Response<GeneralResponse<JsonObject>> response = refreshCall.execute();
        // Get the new access token
        String newAccessToken = response.body().getWrappedData().get("accessToken").getAsString();
        SharedPreferencesHelper.putStringValue(Constants.USER_ACCESS_TOKEN, newAccessToken); // Storage in shared preferences

        Timber.i("Token refreshed");
        Timber.d("New token: %s", newAccessToken);

        return newAccessToken;
    }


    /**
     * We analise the response body here.
     * We have to make a 'dirty hack' for read the body of the response twice
     * without closing the response scope.
     * <p>
     * We have to do this because we need to read the status code of the error node.
     * This error node is in the response body in this way:
     * <p>
     * {
     * "ok": false,
     * "error": {
     * "message": "Token expirado",
     * "code": 502                       <---- This is the value that we have to read
     * }
     * }
     * <p>
     * <p>
     * I use the solution that I found in this github issue:
     * https://github.com/square/okhttp/issues/1240#issuecomment-330813274
     *
     * @return true when the token is expired
     */
    public boolean isTokenExpired(Response response) {

        if (response.code() == 401) {
            try {
                ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                String content = responseBodyCopy.string();

                JSONObject jsonObject = new JSONObject(content);
                Timber.d(jsonObject.toString());
                String errorCode = jsonObject.getJSONObject("error").getString("code");
                if (errorCode.equals("502")) {
                    return true;
                }

            } catch (IOException | JSONException | NullPointerException e) {
                Timber.e(e);

            }
        }
        return false;

    }

}
