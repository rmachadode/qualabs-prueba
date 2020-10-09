package com.qualabs.app.model;

import com.google.gson.annotations.SerializedName;

public class Provider {

	@SerializedName("content_module")
	private String contentModule;

	@SerializedName("auth_module")
	private String authModule;

	public String getContentModule() {
		return contentModule;
	}

	public void setContentModule(String contentModule) {
		this.contentModule = contentModule;
	}

	public String getAuthModule() {
		return authModule;
	}

	public void setAuthModule(String authModule) {
		this.authModule = authModule;
	}

}
