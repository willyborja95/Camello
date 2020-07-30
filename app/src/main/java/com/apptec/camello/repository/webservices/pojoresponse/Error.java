package com.apptec.camello.repository.webservices.pojoresponse;

import com.google.gson.annotations.SerializedName;

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
public class Error {


    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
