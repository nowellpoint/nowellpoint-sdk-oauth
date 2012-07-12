package com.redhat.sforce.persistence;

import java.util.Locale;

public class ConnectionProperties {

	private static Locale LOCALE;
	private static String SERVICE_ENDPOINT;
	private static String API_VERSION;
	private static String API_ENDPOINT;	
	private static String FRONT_DOOR_URL;
	
	public static String getApiVersion() {
		return API_VERSION;
	}

	public static void setApiVersion(String apiVersion) {
		API_VERSION = apiVersion;
	}

	public static String getApiEndpoint() {
		return API_ENDPOINT;
	}

	public static void setApiEndpoint(String apiEndpoint) {
		API_ENDPOINT = apiEndpoint;
	}

	public static String getFrontDoorUrl() {
		return FRONT_DOOR_URL;
	}

	public static void setFrontDoorUrl(String frontDoorUrl) {
		FRONT_DOOR_URL = frontDoorUrl;
	}

	public static void setServiceEndpoint(String serviceEndpoint) {
		SERVICE_ENDPOINT = serviceEndpoint;
	}
	
	public static String getServiceEndpoint() {
		return SERVICE_ENDPOINT;
	}	
	
	public static Locale getLocale() {
		return LOCALE;
	}

	public static void setLocale(Locale locale) {
		LOCALE = locale;
	}
}