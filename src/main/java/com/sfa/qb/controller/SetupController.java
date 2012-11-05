package com.sfa.qb.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.sfa.qb.model.entities.Configuration;
import com.sfa.qb.model.setup.SetupUser;
import com.sfa.qb.qualifiers.Create;
import com.sfa.qb.qualifiers.MessageBundle;
import com.sfa.qb.qualifiers.Reset;
import com.sfa.qb.qualifiers.Update;
import com.sfa.qb.service.PersistenceService;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.fault.LoginFault;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

@Model

public class SetupController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Logger log;
	
	@Inject
	private PersistenceService persistenceService;
	
	@Inject
	private FacesContext context;
	
	@Inject
	@MessageBundle
	private ResourceBundle messages;
	    
	@Inject
	private Event<Configuration> configurationEvent;
	
	@Inject
	private MainController mainController;
	
	@SuppressWarnings("serial")
	private static final AnnotationLiteral<Update> UPDATE_CONFIGURATION  = new AnnotationLiteral<Update>() {};
	
	@SuppressWarnings("serial")
	private static final AnnotationLiteral<Reset> RESET_CONFIGURATION  = new AnnotationLiteral<Reset>() {};
	
	@SuppressWarnings("serial")
	private static final AnnotationLiteral<Create> CREATE_CONFIGURATION  = new AnnotationLiteral<Create>() {};
	
	private static final String AUTH_ENDPOINT = "{0}/services/Soap/u/";
	
	private static final String API_VERSION = "26.0";
	
	
	@PostConstruct
	public void init() {
		log.info("init");
	}
	
	public void editConfiguration(Configuration configuration) {
		configuration.setEditable(Boolean.TRUE);
	}
	
	public void testConfiguration(Configuration configuration) {
					
		ConnectorConfig config = new ConnectorConfig();
    	config.setAuthEndpoint(configuration.getAuthEndpoint());
    	config.setUsername(configuration.getUsername());
		config.setPassword(configuration.getPassword() + configuration.getSecurityToken());

		FacesMessage message = null;
		PartnerConnection connection = null;
		try {					
			connection = Connector.newConnection(config);	
			configuration.setIsActive(Boolean.TRUE);
			configuration.setServiceEndpoint(connection.getConfig().getServiceEndpoint());
			configuration.setApiEndpoint(connection.getConfig().getServiceEndpoint().substring(0, connection.getConfig().getServiceEndpoint().indexOf("/Soap")));
																	
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, null, messages.getString("success"));
			
		} catch (LoginFault lf) {
			
			configuration.setAuthEndpoint(null);
			configuration.setServiceEndpoint(null);
			configuration.setApiEndpoint(null);	
			            
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, lf.getExceptionMessage());
			
		} catch (ConnectionException ce) {
			
			configuration.setAuthEndpoint(null);
			configuration.setServiceEndpoint(null);
			configuration.setApiEndpoint(null);	
			            
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ce.getMessage());
			
		}				
		
		configurationEvent.select(UPDATE_CONFIGURATION).fire(configuration);
				
		context.addMessage(FacesContext.getCurrentInstance().getViewRoot().findComponent("mainForm:testButton").getClientId(), message);
	}
	
	public void saveConfiguration(Configuration configuration) {
		
        if (configuration.getCreatedDate() == null) {
            configuration.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        }
  
        configuration.setLastModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		configuration.setEditable(Boolean.FALSE);
		configuration = persistenceService.saveConfiguration(configuration);
				
		configurationEvent.select(UPDATE_CONFIGURATION).fire(configuration);		
	}
	
	public void resetConfiguration(Configuration configuration) {
		configurationEvent.select(RESET_CONFIGURATION).fire(configuration);			
	}
	
	public void login(SetupUser setupUser) {
		
		/**
		 * format the authEndpoint string
		 */
		
		String authEndpoint = MessageFormat.format(AUTH_ENDPOINT + API_VERSION, setupUser.getInstance());				

		/**
		 * create the ConnectorConfig object for the PartnerConnection
		 */
		
		ConnectorConfig config = new ConnectorConfig();
    	config.setAuthEndpoint(authEndpoint);
    	config.setUsername(setupUser.getUsername());
		config.setPassword(setupUser.getPassword() + setupUser.getSecurityToken());

		PartnerConnection connection = null;
		try {		
			
			/**
			 * log into Salesforce 
			 */
			
			connection = Connector.newConnection(config);		
			
			/**
			 * create the Configuration object
			 */
			
			Configuration configuration = new Configuration();
			configuration.setInstance(setupUser.getInstance());
			configuration.setAuthEndpoint(authEndpoint);					
	        configuration.setCreatedById(connection.getUserInfo().getUserId());	  
		    configuration.setLastModifiedById(connection.getUserInfo().getUserId());
		    configuration.setEditable(Boolean.TRUE);
		    
		    /**
		     * clear values from the setup user
		     */
		    
		    setupUser.setInstance(null);
		    setupUser.setUsername(null);
		    setupUser.setSecurityToken(null);
		    setupUser.setPassword(null);
		    setupUser.setStatus(null);
		    
		    /**
		     * invoke the @Create Observer method in the Producer class
		     */
		    configurationEvent.select(CREATE_CONFIGURATION).fire(configuration);
		    
		    /**
		     * route to the salesforce configuration page
		     */
		    
		    mainController.setMainArea(TemplatesEnum.SETUP);
		    
		} catch (LoginFault lf) {			            
			setupUser.setStatus(lf.getExceptionMessage());
			return;
			
		} catch (ConnectionException ce) {			            
			setupUser.setStatus(ce.getMessage());
			return;
			
		}								
	}
}