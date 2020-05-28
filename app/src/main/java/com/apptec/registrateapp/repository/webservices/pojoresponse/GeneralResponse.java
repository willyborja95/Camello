package com.apptec.registrateapp.repository.webservices.pojoresponse;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse<T> {

    @SerializedName("data")
    private WrapperData<T> wrapperData;

    @SerializedName("ok")
    @Nullable
    private boolean ok;

    @SerializedName("error")
    @Nullable
    private Error error;

    public WrapperData<T> getWrapperData() {
        return wrapperData;
    }

    public void setWrapperData(WrapperData<T> wrapperData) {
        this.wrapperData = wrapperData;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Nullable
    public Error getError() {
        return error;
    }

    public void setError(@Nullable Error error) {
        this.error = error;
    }
}
