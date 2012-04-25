package com.redhat.sforce.qb.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.joda.time.DateTime;

import com.redhat.sforce.qb.dao.QuoteDAO;
import com.redhat.sforce.qb.exception.SalesforceServiceException;
import com.redhat.sforce.qb.manager.SessionManager;
import com.redhat.sforce.qb.model.Contact;
import com.redhat.sforce.qb.model.Opportunity;
import com.redhat.sforce.qb.model.OpportunityLineItem;
import com.redhat.sforce.qb.model.Quote;
import com.redhat.sforce.qb.model.QuoteLineItem;
import com.redhat.sforce.qb.model.QuoteLineItemPriceAdjustment;
import com.redhat.sforce.qb.model.QuotePriceAdjustment;
import com.redhat.sforce.qb.model.User;
import com.redhat.sforce.qb.util.FacesUtil;
import com.redhat.sforce.qb.util.ListQuotes;
import com.redhat.sforce.qb.util.QueryQuote;
import com.redhat.sforce.qb.util.SelectedQuote;
import com.redhat.sforce.qb.util.ViewQuote;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.ws.ConnectionException;

@Model

public class QuoteController {

	@Inject
	private Logger log;

	@Inject
	private SessionManager sessionManager;		
		
	@Inject
	private QuoteDAO quoteDAO;
	
	@Inject
	@SelectedQuote
	private Quote selectedQuote;

	@Inject
	@ListQuotes
	private Event<Quote> listQuotesEvent;
	
	@Inject
	@ViewQuote
	private Event<Quote> viewQuoteEvent;
	
	@Inject
	@QueryQuote
	private Event<Quote> queryQuoteEvent;

	@Inject
	private Event<User> userEvents;

	@PostConstruct
	public void init() {
		log.info("init");		 
	}
	
	public void setEditMode(Boolean editMode) {
		sessionManager.setEditMode(editMode);
	}
	
	public Boolean getEditMode() {
		return sessionManager.getEditMode();
	}

	public void setMainArea(TemplatesEnum mainArea) {
		sessionManager.setMainArea(mainArea);
	}

	public TemplatesEnum getMainArea() {
		return sessionManager.getMainArea();
	}

	public void refresh() {
		userEvents.fire(new User());
		listQuotesEvent.fire(new Quote());		
	}

	public void logout() {	
		
		try {
			sessionManager.getPartnerConnection().logout();
		} catch (ConnectionException e) {
			throw new FacesException(e);
		}		
							
       	HttpSession session = FacesUtil.getSession();
       	session.invalidate();
       	
       	setMainArea(TemplatesEnum.HOME);
	}
	
	public void login() {
		
		HttpSession session = FacesUtil.getSession();
       	session.invalidate();
		
	    try {
		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		    externalContext.redirect(externalContext.getRequestContextPath() + "/authorize");
	    } catch (IOException e) {
		    e.printStackTrace();
	    } 
	}

	public void backToQuoteManager() {
		setMainArea(TemplatesEnum.QUOTE_MANAGER);
	}

	public void backToViewQuote() {
		setMainArea(TemplatesEnum.QUOTE_DETAILS);
	}

	public void newQuote(Opportunity opportunity) {
		Quote quote = new Quote(opportunity);
		viewQuoteEvent.fire(quote);
		setEditMode(Boolean.TRUE);
		setMainArea(TemplatesEnum.QUOTE_DETAILS);
	}

	public void editQuote() {
		setEditMode(Boolean.TRUE);
	}
	
	public void priceQuote() {
		priceQuote(selectedQuote);
	}
	
	public void priceQuote(Quote quote) {
		try {
			quoteDAO.priceQuote(quote.getId());
		} catch (SalesforceServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void activateQuote() {
		activateQuote(selectedQuote);
	}

	public void activateQuote(Quote quote) {
		try {
			quoteDAO.activateQuote(quote.getId());
			listQuotesEvent.fire(quote);
		} catch (SalesforceServiceException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	public void copyQuote() {
		copyQuote(selectedQuote);
	}

	public void copyQuote(Quote quote) {
		try {
			quoteDAO.copyQuote(quote.getId());
			listQuotesEvent.fire(new Quote());
		} catch (SalesforceServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void viewQuote(Quote quote) {
		viewQuoteEvent.fire(quote);		
		setMainArea(TemplatesEnum.QUOTE_DETAILS);
	}

	public void deleteQuote() {
		deleteQuote(selectedQuote);		
		setMainArea(TemplatesEnum.QUOTE_MANAGER);
	}

	public void deleteQuote(Quote quote) {
		DeleteResult deleteResult = null;
		try {
			deleteResult = quoteDAO.deleteQuote(quote);			
			if (deleteResult.isSuccess()) {
				log.info("Quote " + quote.getId() + " has been deleted");
				listQuotesEvent.fire(quote);				
			} else {
				log.error("Quote delete failed: " + deleteResult.getErrors()[0].getMessage());
			}
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void calculateQuote() {
		calculateQuote(selectedQuote);
	}

	public void calculateQuote(Quote quote) {
		try {
			quoteDAO.calculateQuote(quote.getId());
			queryQuoteEvent.fire(quote);
		} catch (SalesforceServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMainArea(TemplatesEnum.QUOTE_DETAILS);
	}

	public void save() {				
		saveQuotePriceAdjustments();
		saveQuoteLineItemPriceAdjustments();
		saveQuoteLineItems();
		saveQuote();
		setEditMode(Boolean.FALSE);
		setMainArea(TemplatesEnum.QUOTE_DETAILS);
	}

	public void saveQuote() {
		try {
			SaveResult saveResult = quoteDAO.saveQuote(selectedQuote); 
			if (saveResult.isSuccess() && saveResult.getId() != null) {
				selectedQuote.setId(saveResult.getId());
				queryQuoteEvent.fire(selectedQuote);	
				FacesUtil.addInformationMessage("Record saved successfully");
			} else {
				log.error("Quote save failed: " + saveResult.getErrors()[0].getMessage());
				FacesUtil.addErrorMessage(saveResult.getErrors()[0].getMessage());
			}
			
		} catch (ConnectionException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} 
	}

	public void saveQuoteLineItems() {
		if (selectedQuote.getQuoteLineItems() == null)
			return;

		List<QuoteLineItem> quoteLineItems = new ArrayList<QuoteLineItem>();
		for (QuoteLineItem quoteLineItem : selectedQuote.getQuoteLineItems()) {
			if (quoteLineItem.getPricebookEntryId() != null) {
				quoteLineItems.add(quoteLineItem);
			}
		}

		if (quoteLineItems.size() == 0)
			return;

		try {
			quoteDAO.saveQuoteLineItems(selectedQuote, selectedQuote.getQuoteLineItems());
	
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SalesforceServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveQuotePriceAdjustments() {
		if (selectedQuote.getQuotePriceAdjustments() == null)
			return;
		
		try {
			quoteDAO.saveQuotePriceAdjustments(selectedQuote.getQuotePriceAdjustments());
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void saveQuoteLineItemPriceAdjustments() {
		if (selectedQuote.getQuotePriceAdjustments() == null || selectedQuote.getQuoteLineItems() == null)
			return;
		
		List<QuoteLineItemPriceAdjustment> quoteLineItemPriceAdjustmentList = new ArrayList<QuoteLineItemPriceAdjustment>();
		
		for (QuoteLineItem quoteLineItem : selectedQuote.getQuoteLineItems()) {
		    for (QuotePriceAdjustment quotePriceAdjustment : selectedQuote.getQuotePriceAdjustments()) {		    
		    	if (quotePriceAdjustment.getReason().equals(quoteLineItem.getProduct().getPrimaryBusinessUnit())) {
		    		QuoteLineItemPriceAdjustment quoteLineItemPriceAdjustment = new QuoteLineItemPriceAdjustment();
		    		quoteLineItemPriceAdjustment.setQuoteLineItemId(quoteLineItem.getId());
		    		quoteLineItemPriceAdjustment.setQuoteId(selectedQuote.getId());		    		
		    		quoteLineItemPriceAdjustment.setOperator(quotePriceAdjustment.getOperator());
		    		quoteLineItemPriceAdjustment.setPercent(quotePriceAdjustment.getPercent());
		    		quoteLineItemPriceAdjustment.setReason(quotePriceAdjustment.getReason());
		    		quoteLineItemPriceAdjustment.setType(quotePriceAdjustment.getType());		    		
		    		
		    		BigDecimal amount = new BigDecimal(0.00);
		    		amount = new BigDecimal(quoteLineItemPriceAdjustment.getPercent()).multiply(new BigDecimal(.01));
				    amount = amount.multiply(new BigDecimal(quoteLineItem.getYearlySalesPrice())).setScale(2, RoundingMode.HALF_EVEN);
				    quoteLineItemPriceAdjustment.setAmount(amount.doubleValue());		
				    
				    quoteLineItem.setYearlySalesPrice(new BigDecimal(quoteLineItem.getYearlySalesPrice()).subtract(amount).doubleValue());
		    		
		    		quoteLineItemPriceAdjustmentList.add(quoteLineItemPriceAdjustment);
		    		continue;
		    	}		    
		    }		    	
		}
		
		try {
			quoteDAO.saveQuoteLineItemPriceAdjustments(quoteLineItemPriceAdjustmentList);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void cancelEdit() {
		if (selectedQuote.getId() != null) {
			queryQuoteEvent.fire(selectedQuote);
			setMainArea(TemplatesEnum.QUOTE_DETAILS);
		} else {
			setMainArea(TemplatesEnum.QUOTE_MANAGER);
		}
		setEditMode(Boolean.FALSE);
	}

	public void viewOpportunityLineItems() {
		setMainArea(TemplatesEnum.OPPORTUNITY_LINE_ITEMS);
	}

	public void addOpportunityLineItems(Opportunity opportunity) {
		List<OpportunityLineItem> opportunityLineItemList = new ArrayList<OpportunityLineItem>();
		for (OpportunityLineItem opportunityLineItem : opportunity.getOpportunityLineItems()) {
			if (opportunityLineItem.isSelected()) {
				opportunityLineItemList.add(opportunityLineItem);
				opportunityLineItem.setSelected(false);
			}
		}

		try {
			quoteDAO.addOpportunityLineItems(selectedQuote, opportunityLineItemList);
			queryQuoteEvent.fire(selectedQuote);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SalesforceServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setMainArea(TemplatesEnum.QUOTE_DETAILS);
	}

	public void newQuoteLineItem() {
		selectedQuote.getQuoteLineItems().add(new QuoteLineItem(selectedQuote));
	}
	
	public void deleteQuoteLineItem(QuoteLineItem quoteLineItem) {
		try {
			quoteDAO.deleteQuoteLineItem(quoteLineItem);
			queryQuoteEvent.fire(selectedQuote);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteQuoteLineItems() {
		if (selectedQuote.getQuoteLineItems() == null)
			return;

		List<QuoteLineItem> quoteLineItems = new ArrayList<QuoteLineItem>();
		for (QuoteLineItem quoteLineItem : selectedQuote.getQuoteLineItems()) {
			if (quoteLineItem.getDelete()) {
				quoteLineItems.add(quoteLineItem);
			}
		}

		if (quoteLineItems.size() == 0)
			return;

		try {
			quoteDAO.deleteQuoteLineItems(quoteLineItems);
			queryQuoteEvent.fire(selectedQuote);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void setQuoteContact(Contact contact) {
		selectedQuote.setContactId(contact.getId());
		selectedQuote.setContactName(contact.getName());
	}

	public void setQuoteOwner(User user) {
		selectedQuote.setOwnerId(user.getId());
		selectedQuote.setOwnerName(user.getName());
	}
	
	public void setDates() {
		log.info("new Start date: " + selectedQuote.getStartDate());
		Quote quote = selectedQuote;
		if ("Standard".equals(quote.getType())) {
			DateTime dt = new DateTime(quote.getStartDate());			
			quote.setEndDate(dt.plusDays(quote.getTerm()).minusDays(1).toDate());
			log.info("new End date: " + quote.getEndDate());
		    for (QuoteLineItem quoteLineItem : quote.getQuoteLineItems()) {
			    quoteLineItem.setStartDate(quote.getStartDate());
			    quoteLineItem.setTerm(quote.getTerm());
			    quoteLineItem.setEndDate(quote.getEndDate());
		    }
		}
		

		
	}
		
	public void applyDiscounts() {
		try {
			quoteDAO.saveQuotePriceAdjustments(selectedQuote.getQuotePriceAdjustments());
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}