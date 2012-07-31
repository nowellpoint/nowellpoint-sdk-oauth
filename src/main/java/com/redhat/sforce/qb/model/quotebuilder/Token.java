package com.redhat.sforce.qb.model.quotebuilder;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Token implements Serializable {

	private static final long serialVersionUID = 3368118911132237013L;

	@SerializedName("id")
	private String id;
	
	@SerializedName("issued_at")	
	private String issuedAt;
	
	@SerializedName("refresh_token")	
	private String refreshToken;
	
	@SerializedName("instance_url")
	private String instanceUrl;
	
	@SerializedName("signature")
	private String signature;
	
	@SerializedName("access_token")
	private String accessToken;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
