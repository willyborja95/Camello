package com.apptec.camello.loginactivity.forgotpassword;

import com.google.gson.annotations.SerializedName;

public class RecoverPasswordBody {

    @SerializedName("email")
    private String userEmail;

    public RecoverPasswordBody(String userEmail) {
        this.userEmail = userEmail;
    }
}
