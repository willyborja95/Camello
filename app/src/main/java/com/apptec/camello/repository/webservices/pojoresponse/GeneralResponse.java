package com.apptec.camello.repository.webservices.pojoresponse;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class GeneralResponse<T> {

    @SerializedName("data")
    private T wrappedData; // This will the node that could change according to the call

    @SerializedName("ok")
    @Nullable
    private boolean ok;

    @SerializedName("error")
    @Nullable
    private Error error;

    public T getWrappedData() {
        return wrappedData;
    }

    public void setWrappedData(T wrappedData) {
        this.wrappedData = wrappedData;
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

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "wrappedData=" + wrappedData +
                ", ok=" + ok +
                ", error=" + error +
                '}';
    }
}
