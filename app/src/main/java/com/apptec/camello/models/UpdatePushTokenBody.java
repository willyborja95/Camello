package com.apptec.camello.models;


import com.google.gson.annotations.SerializedName;

public class UpdatePushTokenBody {

    @SerializedName("pushToken")
    private String pushToken;

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    @Override
    public String toString() {
        return "UpdatePushTokenBody{" +
                "pushToken='" + pushToken + '\'' +
                '}';
    }
}
