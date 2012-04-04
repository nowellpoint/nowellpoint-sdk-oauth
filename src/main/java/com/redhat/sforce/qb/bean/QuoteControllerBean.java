package com.redhat.sforce.qb.bean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.json.JSONException;

import com.redhat.sforce.qb.manager.SessionManager;
import com.redhat.sforce.qb.model.Contact;
import com.redhat.sforce.qb.model.Opportunity;
import com.redhat.sforce.qb.model.OpportunityLineItem;
import com.redhat.sforce.qb.model.Quote;
import com.redhat.sforce.qb.model.QuoteLineItem;
import com.redhat.sforce.qb.model.User;
import com.redhat.sforce.qb.service.exception.SforceServiceException;

@ManagedBean(name="quoteController")
@ViewScoped

public class QuoteControllerBean implements QuoteController {
	
	@ManagedProperty(value="#{sessionManager}")
    private SessionManager sessionManager;
			
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	private Opportunity opportunity;
	private List<Quote> quoteList;
	private Quote selectedQuote;	
	
	
	private String page = "quotelist.xhtml";
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	@Inject
	Logger log;

	@Override
	public Opportunity getOpportunity() {			
		if (opportunity == null) {
			try {
				setOpportunity(sessionManager.queryOpportunity());
			} catch (SforceServiceException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (JSONException e) {
				log.error(e);
			} catch (ParseException e) {
				log.error(e);
			}
		}
		return opportunity;
	}
	
	@Override
	public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;		
	} 

	@Override
	public List<Quote> getQuoteList() {		
		if (quoteList == null) {			
			try {			
			 	quoteList = sessionManager.queryQuotes();
			} catch (SforceServiceException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (JSONException e) {
				log.error(e);
			} catch (ParseException e) {
				log.error(e);
			}
		}
		return quoteList;
	}
	
	@Override
	public void refresh() {
        opportunity = null;
        quoteList = null;
	}
	
	@Override
	public Quote getActiveQuote() {
		for (Quote quote : getQuoteList()) {
			if (quote.getIsActive()) { 
				return quote;
			}
		}
		
		return null;
	}
	
	@Override
	public void setQuoteList(List<Quote> quoteList) {
		this.quoteList = quoteList;				
	}	

	@Override
	public Quote getSelectedQuote() {
		return selectedQuote;
	}

	@Override
	public void setSelectedQuote(Quote selectedQuote) {
		this.selectedQuote = selectedQuote;
	}
	
	@Override
	public void newQuote() {
		Quote quote = new Quote(getOpportunity());
		setSelectedQuote(quote);
	}
	
	@Override
	public void activateQuote() {
		activateQuote(getSelectedQuote());
	}
	
	@Override
	public void activateQuote(Quote quote) {		
		sessionManager.activateQuote(quote);
		setQuoteList(null);
	}
	
	@Override
	public void copyQuote() {	
		copyQuote(getSelectedQuote());
	}
	
	@Override
	public void copyQuote(Quote quote) {
		sessionManager.copyQuote(quote);
		setQuoteList(null);		
	}
	
	@Override
	public void editQuote(Quote quote) {
		setSelectedQuote(quote);
		setPage("quotedetails.xhtml");
	}
	
	@Override
	public void deleteQuote() {
		deleteQuote(getSelectedQuote());
	}
	
	@Override
	public void deleteQuote(Quote quote) {			
		sessionManager.deleteQuote(quote);				
		setQuoteList(null);
		setSelectedQuote(null);
	}
	
	@Override
	public void calculateQuote() {
		sessionManager.calculateQuote(getSelectedQuote().getId());
		setQuoteList(null);
	}
	
	@Override
	public void save() {
		saveQuote();
		saveQuoteLineItems();
		setQuoteList(null);		
	}
	
	@Override
	public void saveQuote() {	
		try {
		    sessionManager.saveQuote(getSelectedQuote());  
		    
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}
	
	@Override
	public void saveQuoteLineItems() {
		if (getSelectedQuote().getQuoteLineItems() == null)
			return;
		
		List<QuoteLineItem> quoteLineItems = new ArrayList<QuoteLineItem>();
		for (QuoteLineItem quoteLineItem : getSelectedQuote().getQuoteLineItems()) {
			if (quoteLineItem.getPricebookEntryId() != null) {
				quoteLineItems.add(quoteLineItem);	
		    }
		}
		
		if (quoteLineItems.size() == 0)
			return;
			
		try {
		    sessionManager.saveQuoteLineItems(getSelectedQuote().getQuoteLineItems());
			    
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}
	
	@Override
	public void reset() {
		String quoteId = getSelectedQuote().getId();
		if (quoteId != null) {
			try {
				setSelectedQuote(sessionManager.queryQuote(quoteId));
				setQuoteList(null);

			} catch (SforceServiceException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} else {			
			setSelectedQuote(null);
		}
	}
	
	@Override
	public void addOpportunityLineItems(List<OpportunityLineItem> opportunityLineItems) {
		try {
			sessionManager.addOpportunityLineItems(getSelectedQuote(), opportunityLineItems);	
			setQuoteList(null);	
			
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}	
	}
	
	@Override
	public void newQuoteLineItem() {
		getSelectedQuote().getQuoteLineItems().add(new QuoteLineItem(getSelectedQuote()));				
	}
	
	@Override
	public void deleteQuoteLineItems() {
		if (getSelectedQuote().getQuoteLineItems() == null)
			return;
		
		List<QuoteLineItem> quoteLineItems = new ArrayList<QuoteLineItem>();
		for (QuoteLineItem quoteLineItem : getSelectedQuote().getQuoteLineItems()) {
			if (quoteLineItem.getDelete()) {
				quoteLineItems.add(quoteLineItem);	
		    }
		}
		
		if (quoteLineItems.size() == 0)
			return;
		
		try {
		    sessionManager.deleteQuoteLineItems(quoteLineItems);
		    setQuoteList(null);	
		    
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	@Override
	public void setQuoteContact(Contact contact) {
		getSelectedQuote().setContactId(contact.getId());
		getSelectedQuote().setContactName(contact.getName());
	}
	
	@Override
	public void setQuoteOwner(User user) {
		getSelectedQuote().setOwnerId(user.getId());
		getSelectedQuote().setOwnerName(user.getName());
	}	
	

}