package com.apptec.registrateapp.repository.webservices.pojoresponse.loginresponse;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class LoginResponse{

    @SerializedName("data")
    private Data data;

    @SerializedName("ok")
    private boolean ok;

    public void setData(Data data){
        this.data = data;
    }

    public Data getData(){
        return data;
    }

    public void setOk(boolean ok){
        this.ok = ok;
    }

    public boolean isOk(){
        return ok;
    }

    @Override
    public String toString(){
        return
                "LoginResponse{" +
                        "data = '" + data + '\'' +
                        ",ok = '" + ok + '\'' +
                        "}";
    }
}