package com.appTec.RegistrateApp.repository.webServices;

import java.io.IOException;
import java.lang.reflect.Proxy;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {



    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        // Null indicates no attempt to authenticate.
        return null;
    }



    @Override
    public Request authenticate(Route route, Response response) throws IOException {
//        // Refresh your access_token using a synchronous api request
//        newAccessToken = service.refreshToken();
//
//        // Add new header to rejected request and retry it
//        return response.request().newBuilder()
//                .header(AUTHORIZATION, newAccessToken)
//                .build();
        return null;
    }
}