package com.apptec.camello.loginactivity;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {

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