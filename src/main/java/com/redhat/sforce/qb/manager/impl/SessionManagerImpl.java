package com.redhat.sforce.qb.manager.impl;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.json.JSONException;

import com.redhat.sforce.qb.controller.MainArea;
import com.redhat.sforce.qb.controller.PagesEnum;
import com.redhat.sforce.qb.dao.OpportunityDAO;
import com.redhat.sforce.qb.dao.PricebookEntryDAO;
import com.redhat.sforce.qb.dao.QuoteDAO;
import com.redhat.sforce.qb.dao.SessionUserDAO;
import com.redhat.sforce.qb.exception.QuoteBuilderException;
import com.redhat.sforce.qb.exception.SalesforceServiceException;
import com.redhat.sforce.qb.manager.QuoteBuilderManager;
import com.redhat.sforce.qb.manager.SessionManager;
import com.redhat.sforce.qb.model.Opportunity;
import com.redhat.sforce.qb.model.OpportunityLineItem;
import com.redhat.sforce.qb.model.PricebookEntry;
import com.redhat.sforce.qb.model.Quote;
import com.redhat.sforce.qb.model.QuoteLineItem;
import com.redhat.sforce.qb.model.QuotePriceAdjustment;
import com.redhat.sforce.qb.model.User;
import com.redhat.sforce.qb.util.FacesUtil;
import com.redhat.sforce.qb.util.SforceConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

@ManagedBean(name="sessionManager")
@SessionScoped
@Singleton

public class SessionManagerImpl implements Serializable, SessionManager {

	private static final long serialVersionUID = 1L;
	
	private String sessionId;
	
	private String opportunityId;
	
	private PartnerConnection partnerConnection;
			
	@Inject 
	private OpportunityDAO opportunityDAO;
	
	@Inject
	private QuoteDAO quoteDAO;
	
	@Inject
	private PricebookEntryDAO pricebookEntryDAO;
	
	@Inject
	private SessionUserDAO sessionUserDAO;	
			
	@Inject 
	private Logger log;
	
	@Inject 
	private QuoteBuilderManager properties;
	
	@Produces
	@SforceConnection
	@Named
	public PartnerConnection getPartnerConnection() {
		return partnerConnection;
	}
	
	@PostConstruct
	public void init() {					
        log.info("init");				
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String sessionId = session.getAttribute("SessionId").toString();
        if (sessionId != null) {
		    setSessionId(sessionId);
		    
		    ConnectorConfig config = new ConnectorConfig();
			config.setManualLogin(true);
			config.setServiceEndpoint(properties.getServiceEndpoint());
			config.setSessionId(sessionId);
			try {
				partnerConnection = Connector.newConnection(config);
			} catch (ConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
        } else {
        	try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }        
	}		
	
	@Override
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;				
	}

	public String getSessionId() {		
		return sessionId;
	}	
	
	@Override
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	
	public String getOpportunityId() {
		return opportunityId;
	}
		
	@Override
	public User queryUser() throws JSONException, QuoteBuilderException {
        return sessionUserDAO.querySessionUser(getSessionId());
	}	
	
	public List<Quote> queryQuotesByOpportunityId() throws SalesforceServiceException, JSONException, ParseException {
    	return quoteDAO.getQuotesByOpportunityId(getSessionId(), getOpportunityId());
	}	
	
	@Override
	public List<Quote> queryQuotes() throws SalesforceServiceException, JSONException, ParseException {
    	return quoteDAO.queryQuotes(getSessionId());
	}	
	
	@Override
	public Opportunity queryOpportunity() throws QuoteBuilderException, JSONException, ParseException {
		return opportunityDAO.getOpportunity(getSessionId(), getOpportunityId());
	}
	
	@Override
	public Quote queryQuote(String quoteId) throws SalesforceServiceException, JSONException, ParseException {
		return quoteDAO.getQuoteById(getSessionId(), quoteId);
	}	

	@Override
	public Quote saveQuote(Quote quote) throws SalesforceServiceException {
		return quoteDAO.saveQuote(getSessionId(), quote);		
	}
	
	@Override
	public Quote activateQuote(Quote quote) throws SalesforceServiceException {
		return quoteDAO.activateQuote(getSessionId(), quote.getId());
	}
	
	@Override
	public void calculateQuote(String quoteId) {
		quoteDAO.calculateQuote(getSessionId(), quoteId);
	}
	
	@Override
	public void deleteQuote(Quote quote) {
		quoteDAO.deleteQuote(getSessionId(), quote.getId());		
	}
	
	@Override
	public void copyQuote(Quote quote) {
		quoteDAO.copyQuote(getSessionId(), quote.getId());
	}
	
	@Override
	public Quote addOpportunityLineItems(Quote quote, List<OpportunityLineItem> opportunityLineItems) throws SalesforceServiceException {
		return quoteDAO.addOpportunityLineItems(getSessionId(), quote.getId(), opportunityLineItems);
	}
	
	@Override
	public PricebookEntry queryPricebookEntry(String pricebookId, String productCode, String currencyIsoCode) throws SalesforceServiceException {
		return pricebookEntryDAO.queryPricebookEntry(getSessionId(), pricebookId, productCode, currencyIsoCode);
	}
		
	@Override
	public void saveQuoteLineItems(List<QuoteLineItem> quoteLineItemList) throws SalesforceServiceException {		
		quoteDAO.saveQuoteLineItems(getSessionId(), quoteLineItemList);	
	}
	
	@Override
	public void saveQuotePriceAdjustments(List<QuotePriceAdjustment> quotePriceAdjustmentList) throws SalesforceServiceException {
		quoteDAO.saveQuotePriceAdjustments(getSessionId(), quotePriceAdjustmentList);
	}
	
	@Override
	public void deleteQuoteLineItems(List<QuoteLineItem> quoteLineItemList) throws SalesforceServiceException {
		quoteDAO.deleteQuoteLineItems(getSessionId(), quoteLineItemList);
	}
}