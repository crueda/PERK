package com.deimos.perk.utils;

/**
 * ParametersGoogle.
 * 
 * @author rojl
 */
public class ParametersGoogle extends ServiceParameters {
	
	private String clientId = "";
	private String overQueryLimit = "OVER_QUERY_LIMIT";
	private String googleKey = "RltyFple2LsNygUrh3akpGyQ-OE=";

	
	public ParametersGoogle(String urlService, String urlProvider, String clientId, String overQueryLimit, String googleKey) {
		super(urlService, urlProvider);
		this.clientId = clientId;
		this.overQueryLimit = overQueryLimit;
		this.googleKey = googleKey;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getOverQueryLimit() {
		return overQueryLimit;
	}

	public void setOverQueryLimit(String overQueryLimit) {
		this.overQueryLimit = overQueryLimit;
	}

	public String getGoogleKey() {
		return googleKey;
	}
}
