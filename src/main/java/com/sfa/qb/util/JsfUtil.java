package com.sfa.qb.util;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JsfUtil {

	public static void addErrorMessage(String errorMessage) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, errorMessage);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void addInformationMessage(String informationMessage) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, null, informationMessage);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public static String getRequestServerName() {
		return getRequest().getServerName();
	}

	public static String getRequestURI() {
		return getRequest().getRequestURI();
	}

	public static String getRequestQueryString() {
		return getRequest().getQueryString();
	}

	public static Locale getRequestLocale() {
		return getRequest().getLocale();
	}

	public static String getRequestUserAgent() {
		return getRequest().getHeader("User-Agent");
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static void sendRedirect(String url) throws IOException {
		getResponse().sendRedirect(getRequest().getContextPath() + url);
	}
}