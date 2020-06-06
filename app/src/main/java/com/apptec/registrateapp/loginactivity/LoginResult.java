package com.apptec.registrateapp.loginactivity;

import androidx.annotation.Nullable;


class LoginResult {
    /**
     * Authentication result : success (user details) or error message.
     */

    @Nullable
    private Boolean success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable Boolean success) {
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