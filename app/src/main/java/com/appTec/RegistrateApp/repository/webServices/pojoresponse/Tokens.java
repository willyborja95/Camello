package com.appTec.RegistrateApp.repository.webServices.pojoresponse;

import com.google.gson.annotations.SerializedName;

public class Tokens{

	@SerializedName("accessToken")
	private String accessToken;

	@SerializedName("refreshToken")
	private String refreshToken;

	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}

	public String getAccessToken(){
		return accessToken;
	}

	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken(){
		return refreshToken;
	}

	@Override
 	public String toString(){
		return 
			"Tokens{" + 
			"accessToken = '" + accessToken + '\'' + 
			",refreshToken = '" + refreshToken + '\'' + 
			"}";
		}
}