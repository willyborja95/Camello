package com.apptec.registrateapp.loginactivity;

import androidx.annotation.Nullable;


public class LoginResult {
    /**
     * Authentication result : success (user details) or error message.
     */

    @Nullable
    private Boolean success;


    @Nullable
    private Integer error, titleError;  // String resource

    public LoginResult(@Nullable Integer titleError, @Nullable Integer error) {
        this.error = error;
        this.titleError = titleError;
    }

    public LoginResult(@Nullable Boolean success) {
        this.success = success;
    }

    @Nullable
    Boolean getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}