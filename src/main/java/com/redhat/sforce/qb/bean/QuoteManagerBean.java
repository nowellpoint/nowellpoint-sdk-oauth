package com.redhat.sforce.qb.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.redhat.sforce.qb.bean.model.Opportunity;
import com.redhat.sforce.qb.bean.model.Contact;
import com.redhat.sforce.qb.bean.model.OpportunityLineItem;
import com.redhat.sforce.qb.bean.model.Quote;
import com.redhat.sforce.qb.bean.model.User;
import com.redhat.sforce.qb.exception.SforceServiceException;

@ManagedBean(name="quoteManager")
@SessionScoped

public class QuoteManagerBean implements Serializable, QuoteManager {

	private static final long serialVersionUID = 1L;
		
	@ManagedProperty(value="#{sforceSession}")
    private SforceSession sforceSession;	
	
	public SforceSession getSforceSession() {
		return sforceSession;
	}

	public void setSforceSession(SforceSession sforceSession) {
		this.sforceSession = sforceSession;
	}

	@Override
	public void refresh() {
		getQuoteForm().queryAllData();
	}
	
	@Override
	public void newQuote(Opportunity opportunity) {
		getQuoteForm().createQuote(opportunity);
	}
	
	@Override
	public void saveQuote(Quote quote) {
		try {
		    if (quote.getId() != null) {
			    updateQuote(quote);
		    } else {
			    createQuote(quote);
		    }
		    refresh();
		    getQuoteForm().toggleEditMode();
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}
	
	@Override
	public void saveQuoteLineItems(Quote quote) {
		try {
		    sforceSession.saveQuoteLineItems(quote.getQuoteLineItems());
		    sforceSession.calculateQuote(quote.getId());
		    refresh();
		    cancelEditQuote();
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	@Override
	public void deleteQuoteLineItems(Quote quote) {		
		try {
		    sforceSession.deleteQuoteLineItems(quote.getQuoteLineItems());
		    sforceSession.calculateQuote(quote.getId());
		    refresh();
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	@Override
	public void cancelEditQuote() {
		getQuoteForm().toggleEditMode();
	}
			
	@Override
	public void addOpportunityLineItems(Opportunity opportunity, Quote quote) {
		String[] opportunityLineIds = new String[opportunity.getOpportunityLineItems().size()];
		for (int i = 0; i < opportunity.getOpportunityLineItems().size(); i++) {
			OpportunityLineItem opportunityLineItem = opportunity.getOpportunityLineItems().get(i);
			opportunityLineIds[i] = opportunityLineItem.getId();
		}
		
		try {
			sforceSession.addOpportunityLineItems(quote, opportunityLineIds);		    
		    refresh();
		} catch (SforceServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}
	
	@Override 
	public void activateQuote(Quote quote) {
		sforceSession.activateQuote(quote);
		refresh();
	}
	
	@Override
	public void calculateQuote(Quote quote) {
		sforceSession.calculateQuote(quote.getId());		
		refresh();
	}
	
	@Override
	public void editQuote(Quote quote) {										
		getQuoteForm().editQuote(quote);
	}
	
	@Override
	public void deleteQuote(Quote quote) {
		sforceSession.deleteQuote(quote);
		refresh();		
		getQuoteForm().setSelectedQuote(null);
	}
	
	@Override
	public void copyQuote(Quote quote) {	
		sforceSession.copyQuote(quote);
		refresh();
	}	
	
	@Override
	public void setQuoteContact(Quote quote, Contact contact) {
		quote.setContactId(contact.getId());
		quote.setContactName(contact.getName());
		saveQuote(quote);
	}
	
	@Override
	public void setQuoteOwner(Quote quote, User user) {
		quote.setOwnerId(user.getId());
		quote.setOwnerName(user.getName());
		saveQuote(quote);
	}	
	
	private void updateQuote(Quote quote) throws SforceServiceException {
		sforceSession.updateQuote(quote);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully updated!", "Succesfully updated!"));
	}
	
	private void createQuote(Quote quote) throws SforceServiceException {			
		sforceSession.createQuote(quote);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully created!", "Succesfully created!"));
	}
			
	private QuoteForm getQuoteForm() {
		return (QuoteForm) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("quoteForm");
	}
}