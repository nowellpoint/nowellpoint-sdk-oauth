package com.sfa.qb.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.sfa.qb.dao.ChatterDAO;
import com.sfa.qb.dao.OpportunityDAO;
import com.sfa.qb.dao.QuoteDAO;
import com.sfa.qb.exception.QueryException;
import com.sfa.qb.exception.SalesforceServiceException;
import com.sfa.qb.model.sobject.Opportunity;
import com.sfa.qb.model.sobject.Quote;
import com.sfa.qb.model.sobject.QuoteLineItem;
import com.sfa.qb.qualifiers.CopyQuote;
import com.sfa.qb.qualifiers.CreateQuote;
import com.sfa.qb.qualifiers.CreateQuoteLineItem;
import com.sfa.qb.qualifiers.DeleteQuote;
import com.sfa.qb.qualifiers.DeleteQuoteLineItem;
import com.sfa.qb.qualifiers.FollowQuote;
import com.sfa.qb.qualifiers.PriceQuote;
import com.sfa.qb.qualifiers.SelectedQuote;
import com.sfa.qb.qualifiers.UnfollowQuote;
import com.sfa.qb.qualifiers.UpdateQuote;
import com.sfa.qb.qualifiers.UpdateQuoteAmount;
import com.sfa.qb.qualifiers.ViewQuote;

@SessionScoped

public class QuoteProducer implements Serializable {

	private static final long serialVersionUID = 7525581840655605003L;

	@Inject
	private Logger log;
	
	@Inject
	private OpportunityDAO opportunityDAO;
	
	@Inject
	private QuoteDAO quoteDAO;
	
	@Inject
	private ChatterDAO chatterDAO;
		
	@Inject
	private List<Quote> quoteList;

	private Quote selectedQuote;

	@Produces
    @SelectedQuote
	@Named
    @Dependent
	public Quote getSelectedQuote() {
		return selectedQuote;
	}

	public void onViewQuote(@Observes @ViewQuote final Quote quote) {
		selectedQuote = queryQuoteById(quote.getId()); 	
		try {
			selectedQuote.setFollowers(chatterDAO.getQuoteFollowers(quote.getId()));
			chatterDAO.getFeedForQuote(quote.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void onCreateQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @CreateQuote final Quote quote) {
		selectedQuote = queryQuoteById(quote.getId());
		selectedQuote.setOpportunity(queryOpportunity(quote.getOpportunity().getId()));
		quoteList.add(selectedQuote);
	}
	
	public void onUpdateQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @UpdateQuote final Quote quote) {
		int index = getQuoteIndex(quote.getId());
		selectedQuote = queryQuoteById(quote.getId());
		selectedQuote.setOpportunity(queryOpportunity(quote.getOpportunity().getId()));
		quoteList.set(index, selectedQuote);
	}
	
	public void onCopyQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @CopyQuote final Quote quote) {		
		selectedQuote = queryQuoteById(quote.getId());
		selectedQuote.setOpportunity(queryOpportunity(quote.getOpportunity().getId()));
		quoteList.add(quote);
	}
	
	public void onDeleteQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @DeleteQuote final Quote quote) {
		quoteList.remove(quote);
	}
	
	public void onDeleteQuoteLineItem(@Observes(during=TransactionPhase.AFTER_SUCCESS) @DeleteQuoteLineItem final QuoteLineItem quoteLineItem) {
		selectedQuote.getQuoteLineItems().remove(quoteLineItem);
	}
	
	public void onCreateQuoteLineItem(@Observes(during=TransactionPhase.AFTER_SUCCESS) @CreateQuoteLineItem final QuoteLineItem quoteLineItem) {
		selectedQuote.getQuoteLineItems().add(quoteLineItem);
	}
	
	public void onUpdateQuoteAmount(@Observes(during=TransactionPhase.AFTER_SUCCESS) @UpdateQuoteAmount final Quote quote) {
		int index = getQuoteIndex(quote.getId());
		selectedQuote.setAmount(getQuoteAmount(quote.getId()));
		quoteList.set(index, selectedQuote);
	}
	
	public void onPriceQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @PriceQuote final Quote quote) {
		Map<String, QuoteLineItem> priceResults = getPriceDetails(quote.getId());
		for (QuoteLineItem quoteLineItem : selectedQuote.getQuoteLineItems()) {
			QuoteLineItem newQuoteLineItem = priceResults.get(quoteLineItem.getId());
			quoteLineItem.setListPrice(newQuoteLineItem.getListPrice());
			quoteLineItem.setDescription(newQuoteLineItem.getDescription());
			quoteLineItem.setCode(newQuoteLineItem.getCode());
			quoteLineItem.setMessage(newQuoteLineItem.getMessage());
			if (quoteLineItem.getUnitPrice() == null) {
				quoteLineItem.setUnitPrice(quoteLineItem.getListPrice());
			}
			if (quoteLineItem.getUnitPrice() != null && quoteLineItem.getListPrice() != null) {
				
			}
		}
	}
	
	public void onFollowQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @FollowQuote final Quote quote) {
		selectedQuote.getFollowers().setIsCurrentUserFollowing(Boolean.TRUE);
	}
	
	public void onUnfollowQuote(@Observes(during=TransactionPhase.AFTER_SUCCESS) @UnfollowQuote final Quote quote) {
		selectedQuote.getFollowers().setIsCurrentUserFollowing(Boolean.FALSE);
	}

	private Opportunity queryOpportunity(String opportunityId) {
		log.info("queryOpportunity: " + opportunityId);		
		try {
			
			return opportunityDAO.queryOpportunityById(opportunityId);
			
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
	}
	
	private Quote queryQuoteById(String quoteId) {
		log.info("queryQuoteById");
		try {
			
			return quoteDAO.queryQuoteById(quoteId);
			
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private Double getQuoteAmount(String quoteId) {
		log.info("getQuoteAmount");
		try {
			
			return quoteDAO.getQuoteAmount(selectedQuote.getId());
			
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;

	}
	
	private Map<String, QuoteLineItem> getPriceDetails(String quoteId) {
		log.info("getPriceDetails");
		try {
			
			return quoteDAO.queryPriceDetails(quoteId);
			
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private QuoteLineItem queryQuoteLineItemById(String quoteLineItemId) {
		log.info("queryQuoteLineItemById");
		try {

			return quoteDAO.queryQuoteLineItemById(quoteLineItemId);
			
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private int getQuoteIndex(String quoteId) {
		for (int i = 0; i < quoteList.size(); i++) {
			Quote quote = quoteList.get(i);
			if (quoteId.equals(quote.getId())) {
				return i;
			}
		}
		return -1;
	}
}