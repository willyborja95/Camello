package com.apptec.registrateapp.repository.webservices.pojoresponse;

import com.google.gson.annotations.SerializedName;

public class Error {
    /**
     * This class will wrap network error responses according to the responses like this:
     * {
     * "ok": false,
     * "error": {
     * "message": "Token expirado",
     * "code": 502
     * }
     * }
     */

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;


}
