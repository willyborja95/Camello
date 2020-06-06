package com.apptec.registrateapp.loginactivity;

import androidx.annotation.Nullable;


class LoginFormState {
    /**
     * Data validation state of the login form.
     */

    @Nullable
    private Integer usernameError; // R.string resource id
    @Nullable
    private Integer passwordError; // R.string resource id

    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}