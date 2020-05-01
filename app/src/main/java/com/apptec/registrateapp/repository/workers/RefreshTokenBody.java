package com.apptec.registrateapp.repository.workers;

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

}
