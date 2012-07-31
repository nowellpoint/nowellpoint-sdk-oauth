package com.redhat.sforce.qb.manager.impl;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.redhat.sforce.persistence.connection.ConnectionManager;
import com.redhat.sforce.persistence.connection.ConnectionProperties;
import com.redhat.sforce.qb.controller.TemplatesEnum;
import com.redhat.sforce.qb.manager.SessionManager;
import com.redhat.sforce.qb.model.quotebuilder.Token;
import com.sforce.ws.ConnectionException;

@Named(value="sessionManager")
@SessionScoped

public class SessionManagerImpl implements Serializable, SessionManager {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;	
	
	private Token token;
	
	private TemplatesEnum mainArea;
	
	private String frontDoorUrl;
	
	private String sessionId;
	
	private Boolean loggedIn;
	
	@Override
	public String getSessionId() {
		return sessionId;
	}
	
	private void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override	
	public Boolean getLoggedIn() {
		return loggedIn;
	}
	
	private void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
			
	@ManagedProperty(value = "false")
	private Boolean editMode;

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Boolean getEditMode() {
		return editMode;
	}
	
	@ManagedProperty(value = "false")
	private Boolean goalSeek;

	public void setGoalSeek(Boolean goalSeek) {
		this.goalSeek = goalSeek;
	}

	public Boolean getGoalSeek() {
		return goalSeek;
	}

	@PostConstruct
	public void init() {
		log.info("init");

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
				
		if (session.getAttribute("Token") != null) {
			
			setToken((Token) session.getAttribute("Token")); 			
			setSessionId(token.getAccessToken());											
			setFrontDoorUrl(ConnectionProperties.getFrontDoorUrl().replace("#sid#", getSessionId()));							
			setLoggedIn(Boolean.TRUE);								
			setMainArea(TemplatesEnum.QUOTE_MANAGER);
			
			session.removeAttribute("Token");									

		} else {
			
			setLoggedIn(Boolean.FALSE);
			setMainArea(TemplatesEnum.SIGN_IN);			
		}			
	}
	
	@PreDestroy
	public void destroy() {
		log.info("destroy");		
	}
	
	@Override
	public void logout() {
		log.info("logout");
		
		try {
			ConnectionManager.openConnection(getSessionId());
			ConnectionManager.logout();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setLoggedIn(Boolean.FALSE);
		setMainArea(TemplatesEnum.HOME);
	}
	
	@Override
	public void login() {
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
       	session.invalidate();
		
	    try {
		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		    externalContext.redirect(externalContext.getRequestContextPath() + "/authorize");
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
		    e.printStackTrace();
	    } 
	}
	
	@Override
	public Token getToken() {
		return token;
	}
	
	private void setToken(Token token) {
		this.token = token;
	}
	
	@Override
	public void setMainArea(TemplatesEnum mainArea) {
		this.mainArea = mainArea;
	}

	@Override
	public TemplatesEnum getMainArea() {
		return mainArea;
	}
	
	@Override
	public String getFrontDoorUrl() {
		return frontDoorUrl;
	}
	
	private void setFrontDoorUrl(String frontDoorUrl) {
		this.frontDoorUrl = frontDoorUrl;
	}
}