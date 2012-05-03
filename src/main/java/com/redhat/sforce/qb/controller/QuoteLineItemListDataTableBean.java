package com.redhat.sforce.qb.controller;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.redhat.sforce.qb.dao.PricebookEntryDAO;
import com.redhat.sforce.qb.model.PricebookEntry;
import com.redhat.sforce.qb.model.Quote;
import com.redhat.sforce.qb.model.QuoteLineItem;
import com.redhat.sforce.qb.qualifiers.SelectedQuote;
import com.redhat.sforce.qb.util.JsfUtil;
import com.sforce.ws.ConnectionException;

@ManagedBean(name = "quoteLineItemListDataTableBean")
@RequestScoped

public class QuoteLineItemListDataTableBean {

	@Inject
	private Logger log;
	
	@Inject
	private PricebookEntryDAO pricebookEntryDAO;

	@Inject
	@SelectedQuote
	private Quote selectedQuote;

	public void validateProduct(AjaxBehaviorEvent event) {
		HtmlInputText inputText = (HtmlInputText) event.getComponent();
		String productCode = inputText.getValue().toString();

		int rowIndex = Integer.valueOf(event.getComponent().getAttributes().get("rowIndex").toString());

		QuoteLineItem quoteLineItem = selectedQuote.getQuoteLineItems().get(rowIndex);		
		try {
			PricebookEntry pricebookEntry = pricebookEntryDAO.queryPricebookEntry(
					selectedQuote.getPricebookId(), 
					productCode, 
					selectedQuote.getCurrencyIsoCode());

			if (pricebookEntry != null) {
				quoteLineItem.setBasePrice(0.00);
				quoteLineItem.setListPrice(pricebookEntry.getUnitPrice());
				quoteLineItem.setDescription(pricebookEntry.getProduct().getDescription());
				quoteLineItem.setPricebookEntryId(pricebookEntry.getId());
				quoteLineItem.setProduct(pricebookEntry.getProduct());
				if (quoteLineItem.getProduct().getConfigurable()) {
					quoteLineItem.setConfiguredSku(productCode);
				}
				
				log.info("PricebookEntry found: " + pricebookEntry.getId());
				
			} else {
				quoteLineItem.setProduct(null);
				quoteLineItem.setBasePrice(null);
				quoteLineItem.setConfiguredSku(null);
				quoteLineItem.setPricebookEntryId(null);
				quoteLineItem.setListPrice(null);
				
				JsfUtil.addErrorMessage(inputText, "invalidSKU", productCode);		
				
				log.info("PricebookEntry not found for: " + productCode);
			}

		} catch (ConnectionException e) {
            throw new FacesException(e);
		}
	}
}