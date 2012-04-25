package com.redhat.sforce.qb.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.redhat.sforce.qb.dao.QuoteDAO;
import com.redhat.sforce.qb.exception.SalesforceServiceException;
import com.redhat.sforce.qb.model.Quote;
import com.redhat.sforce.qb.util.ListQuotes;

import java.io.Serializable;

@SessionScoped

public class QuoteListProducer implements Serializable {

	private static final long serialVersionUID = -8899004949794324741L;

	@Inject
	private Logger log;
	
	@Inject
	private QuoteDAO quoteDAO;

	private List<Quote> quoteList;

	@Produces
	@Named
	public List<Quote> getQuoteList() {
		return quoteList;
	}

	public void onQuoteListChanged(@Observes final @ListQuotes Quote quote) {
		queryQuotes();
	}

	@PostConstruct
	public void queryQuotes() {
		log.info("queryQuotes");
		try {
			quoteList = quoteDAO.queryQuotes();
		} catch (SalesforceServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
