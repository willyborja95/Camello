package com.apptec.camello.auth.refreshtoken;

public class RefreshTokenBody {
    /**
     * This class is used to send a retrofit call with body params
     */

    public String accessToken;
    public String refreshToken;

    public RefreshTokenBody(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshTokenBody{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
