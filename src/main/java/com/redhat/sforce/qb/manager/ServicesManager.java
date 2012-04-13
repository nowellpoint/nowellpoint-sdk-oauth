package com.redhat.sforce.qb.manager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.redhat.sforce.qb.exception.SalesforceServiceException;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;

public interface ServicesManager {

	public JSONObject getCurrentUserInfo();
	public JSONObject getCurrentUserInfo(String accessToken);
	public JSONObject getOpportunity(String opportunityId);
	public JSONObject getOpportunity(String accessToken, String opportunityId);
	
	public JSONArray query(String accessToken, String query) throws SalesforceServiceException;
	public JSONArray query(String query) throws SalesforceServiceException;
	
	public SaveResult create(SObject sobject) throws ConnectionException;
	public SaveResult[] create(SObject[] sobjects) throws ConnectionException;
	public SaveResult update(SObject sobject) throws ConnectionException;
	public SaveResult[] update(SObject[] sobjects) throws ConnectionException;
	
	
	public String saveQuote(String accessToken, JSONObject jsonObject) throws SalesforceServiceException;
	public void saveQuoteLineItems(String accessToken, JSONArray jsonArray) throws SalesforceServiceException;
	public JSONObject queryPricebookEntry(String accessToken, String pricebookId, String productCode, String currencyIsoCode) throws SalesforceServiceException;
	public JSONObject queryPricebookEntry(String pricebookId, String productCode, String currencyIsoCode) throws SalesforceServiceException;
	public JSONArray queryCurrencies(String accessToken) throws SalesforceServiceException;
    public void saveQuotePriceAdjustments(String accessToken, JSONArray jsonArray) throws SalesforceServiceException;
	public void activateQuote(String accessToken, String quoteId) throws SalesforceServiceException;
	public void calculateQuote(String accessToken, String quoteId);	
	public void deleteQuoteLineItems(String accessToken, JSONArray jsonArray) throws SalesforceServiceException;
	public void copyQuote(String accessToken, String quoteId);
	public void deleteQuote(String accessToken, String quoteId);
	public void addOpportunityLineItems(String accessToken, String quoteId, JSONArray jsonArray) throws SalesforceServiceException;
}