package com.sfa.qb.data;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.sfa.persistence.EntityManager;
import com.sfa.persistence.Query;
import com.sfa.persistence.connection.ConnectionManager;
import com.sfa.qb.exception.QueryException;
import com.sfa.qb.model.sobject.CurrencyType;
import com.sforce.ws.ConnectionException;

@SessionScoped

public class CurrencyTypesProducer implements Serializable {

	private static final long serialVersionUID = 1674767844758379451L;
	
	@Inject
	private EntityManager em;

	private List<CurrencyType> currencyTypes;
		
	@PostConstruct
	public void init() {
		queryCurrencyTypes();
	}
	
	@Produces
	@Named
	public List<CurrencyType> getCurrencyTypes() {		
		return currencyTypes;
	}
	
	public void queryCurrencyTypes() {
		
		String queryString = "Select Id, IsoCode from CurrencyType Where IsActive = true";
		
		try {
		    ConnectionManager.openConnection();		
	        Query q = em.createQuery(queryString);	
	        q.orderBy("IsoCode");    		    	        
	        currencyTypes = q.getResultList();	
	        
		} catch (ConnectionException e) {
			//log.error(e);
		} catch (QueryException e) {
			//log.error(e);
		} finally {
			
			try {
				ConnectionManager.closeConnection();
			} catch (ConnectionException e) {
				//log.error("Unable to close connection", e);
			}
		}
	}
}