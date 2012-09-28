package com.deimos.perk.utils;

/**
 * GeocodingParameters.
 * 
 * @author rojl
 *
 */
public abstract class ServiceParameters {
	
	private String urlService = "";
	private String urlProvider = "";
	
	public ServiceParameters(String urlService, String urlProvider) {
		super();
		this.urlService = urlService;
		this.urlProvider = urlProvider;
	}

	public String getUrlService() {
		return urlService;
	}

	public void setUrlService(String urlGeocoding) {
		this.urlService = urlGeocoding;
	}

	public String getUrlProvider() {
		return urlProvider;
	}

	public void setUrlProvider(String urlProvider) {
		this.urlProvider = urlProvider;
	}
	
	

}