package com.nowellpoint.oauth;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.nowellpoint.oauth.callback.OAuthCallbackHandler;
import com.nowellpoint.oauth.callback.OAuthFlowType;
import com.nowellpoint.oauth.model.OrganizationInfo;
import com.nowellpoint.oauth.service.OAuthService;
import com.nowellpoint.oauth.service.impl.OAuthServiceImpl;
import com.nowellpoint.oauth.util.OAuthUtil;

public class OAuthServiceProvider implements Serializable {

	private static final long serialVersionUID = 8065223488307981986L;
	private LoginContext loginContext;
	private Subject subject;
	
    public OAuthServiceProvider(OAuthConfig oauthConfig) {
    	setConfiguration(oauthConfig);
    }
	
	public OAuthServiceProvider(OAuthConfig oauthConfig, Subject subject) {
		setConfiguration(oauthConfig);
        setSubject(subject);        
	}
       
    public void login(HttpServletResponse response) throws LoginException {
    	OAuthConfig oauthConfig = (OAuthConfig) Configuration.getConfiguration();
    	
    	/**
    	 * initialize a new Subject 
    	 */
    	
    	setSubject(new Subject());
    	
    	/**
		 * build the OAuth URL based on the flow from options
		 */
		
		String loginUrl = oauthConfig.buildLoginUrl();
		
		/**
		 * do the redirect
		 */
		
		try {
			response.sendRedirect(loginUrl);
			return;
		} catch (IOException e) {
			throw new LoginException("Unable to do the redirect: " + e);
		}
    }
    
    public void login(FacesContext context) throws LoginException {
        OAuthConfig oauthConfig = (OAuthConfig) Configuration.getConfiguration();
    	
    	/**
    	 * initialize a new Subject 
    	 */
    	
    	setSubject(new Subject());
    	
    	/**
		 * build the OAuth URL based on the flow from options
		 */
		
		String authUrl = oauthConfig.buildLoginUrl();
		
		/**
		 * do the redirect
		 */
		
		try {
			context.getExternalContext().redirect(authUrl);
			return;
		} catch (IOException e) {
			throw new LoginException("Unable to do the redirect: " + e);
		}	
    }
    
    public void authenticate(String username, String password, String securityToken) throws LoginException {    	   	
    	OAuthCallbackHandler callbackHandler = new OAuthCallbackHandler(
    			null,
    			OAuthFlowType.USERNAME_PASSWORD_FLOW,
    			null,
    			null, 
    			username, 
    			password, 
    			securityToken);
		    	    	
    	login(callbackHandler);
    }
    
    public void authenticate(String code) throws LoginException {    	
    	OAuthCallbackHandler callbackHandler = new OAuthCallbackHandler(
    			null,
    			OAuthFlowType.WEB_SERVER_FLOW,
    			code, 
    			null, 
    			null, 
    			null, 
    			null);
    	
    	login(callbackHandler);
    }
    
    public void refreshToken(String refreshToken) throws LoginException {  			
    	OAuthCallbackHandler callbackHandler = new OAuthCallbackHandler(
    			null,
    			OAuthFlowType.REFRESH_TOKEN_FLOW,
    			null, 
    			refreshToken, 
    			null, 
    			null, 
    			null);
    	
    	login(callbackHandler);
    }
    
    public void logout() throws LoginException {
    	loginContext.logout();
    }
    
    public Subject getSubject() {
    	return subject;
    }
    
    public void setSubject(Subject subject) {
    	this.subject = subject;
    }
    
    public String getUserInfo() throws LoginException {
    	String instanceUrl = OAuthUtil.getToken(getSubject()).getInstanceUrl();
    	String accessToken = OAuthUtil.getToken(getSubject()).getAccessToken();
    	String userId = OAuthUtil.getIdentity(getSubject()).getUserId();
    	
    	OAuthService oauthService = new OAuthServiceImpl();
    	String sobject = oauthService.getSObject(OAuthConfig.getUserInfoUrl(instanceUrl, userId), accessToken);
    	
    	System.out.println(sobject);
    	
    	return sobject;
    }
    
    public OrganizationInfo getOrganizationInfo() throws LoginException {
    	String instanceUrl = OAuthUtil.getToken(getSubject()).getInstanceUrl();
    	String accessToken = OAuthUtil.getToken(getSubject()).getAccessToken();
    	String organizationId = OAuthUtil.getIdentity(getSubject()).getOrganizationId();
    	
    	OAuthService oauthService = new OAuthServiceImpl();
    	String sobject = oauthService.getSObject(OAuthConfig.getOrganizationInfoUrl(instanceUrl, organizationId), accessToken);
    	
    	System.out.println(sobject);
    	
    	OrganizationInfo organizationInfo = new Gson().fromJson(sobject, OrganizationInfo.class);
    	
    	return organizationInfo;
    }
    
    private void login(CallbackHandler callbackHander) throws LoginException {
    	loginContext = new LoginContext("OAuth", getSubject(), callbackHander, Configuration.getConfiguration());    	
    	loginContext.login();
    	setSubject(loginContext.getSubject());
    }
   
    private void setConfiguration(OAuthConfig oauthConfig) {							
		Configuration.setConfiguration(oauthConfig);
    }
}