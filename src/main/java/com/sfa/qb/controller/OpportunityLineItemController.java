package com.sfa.qb.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.sfa.qb.model.sobject.OpportunityLineItem;
import com.sfa.qb.model.sobject.Quote;
import com.sfa.qb.qualifiers.SelectedQuote;

@Model

public class OpportunityLineItemController {
	
	@Inject
	private Logger log;	
	
	@Inject
	@SelectedQuote
	private Quote quote;
	
	@PostConstruct
	public void init() {
		log.info("init");
	}
	
	@ManagedProperty(value = "false")
	private Boolean checkedValue;

	public Boolean getCheckedValue() {
		return checkedValue;
	}

	public void setCheckedValue(Boolean checkedValue) {
		this.checkedValue = checkedValue;
	}

	public void toggleCheckboxes(AjaxBehaviorEvent event) {
		HtmlSelectBooleanCheckbox checkBox = (HtmlSelectBooleanCheckbox) event.getComponent();
				
		for (OpportunityLineItem opportunityLineItem : quote.getOpportunity().getOpportunityLineItems()) {
			opportunityLineItem.setSelected(checkBox.isSelected());
		}
		
	}	
}