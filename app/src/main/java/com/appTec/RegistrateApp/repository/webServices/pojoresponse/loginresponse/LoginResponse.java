package com.appTec.RegistrateApp.repository.webServices.pojoresponse.loginresponse;

import com.appTec.RegistrateApp.repository.webServices.pojoresponse.loginresponse.Data;
import com.google.gson.annotations.SerializedName;

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