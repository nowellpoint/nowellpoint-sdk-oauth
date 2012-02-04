package com.redhat.sforce.qb.bean.factory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.redhat.sforce.qb.bean.model.QuotePriceAdjustment;
import com.redhat.sforce.util.JSONObjectWrapper;

public class QuotePriceAdjustmentFactory {
	
	public static List<QuotePriceAdjustment> parseQuotePriceAdjustments(JSONArray jsonArray) throws JSONException, ParseException {
		List<QuotePriceAdjustment> quotePriceAdjustmentList = new ArrayList<QuotePriceAdjustment>();
		
		for (int i = 0; i < jsonArray.length(); i++) {		    
		    JSONObjectWrapper wrapper = new JSONObjectWrapper(jsonArray.getJSONObject(i));
		    
		    QuotePriceAdjustment quotePriceAdjustment = new QuotePriceAdjustment();
		    quotePriceAdjustment.setId(wrapper.getId());
		    quotePriceAdjustment.setCreatedById(wrapper.getString("CreatedBy", "Id"));
		    quotePriceAdjustment.setCreatedByName(wrapper.getString("CreatedBy", "Name"));
		    quotePriceAdjustment.setCreatedDate(wrapper.getDateTime("CreatedDate"));
		    quotePriceAdjustment.setCurrencyIsoCode(wrapper.getString("CurrencyIsoCode"));
		    quotePriceAdjustment.setLastModifiedById(wrapper.getString("LastModifiedBy", "Id"));
		    quotePriceAdjustment.setLastModifiedByName(wrapper.getString("LastModifiedBy", "Name"));
		    quotePriceAdjustment.setLastModifiedDate(wrapper.getDateTime("LastModifiedDate"));
		    quotePriceAdjustment.setAmount(wrapper.getDouble("Amount__c"));
		    quotePriceAdjustment.setOperator(wrapper.getString("Operator__c"));
		    quotePriceAdjustment.setPercent(wrapper.getDouble("Percent__c"));
		    quotePriceAdjustment.setReason(wrapper.getString("Reason__c"));
		    quotePriceAdjustment.setType(wrapper.getString("Type__c"));
		    quotePriceAdjustment.setAmountBeforeAdjustment(wrapper.getDouble("AmountBeforeAdjustment__c"));
		    quotePriceAdjustment.setAmountAfterAdjustment(wrapper.getDouble("AmountAfterAdjustment__c"));
		    
		    quotePriceAdjustmentList.add(quotePriceAdjustment);
		}
		
		return quotePriceAdjustmentList;
	}	
		
	public static JSONArray serializeQuotePriceAdjustments(List<QuotePriceAdjustment> quotePriceAdjustmentList) {
		JSONArray jsonArray = new JSONArray();
		
		for (QuotePriceAdjustment quotePriceAdjustment : quotePriceAdjustmentList) {
			
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("Id", quotePriceAdjustment.getId());
				jsonObject.put("QuoteId__c", quotePriceAdjustment.getQuoteId());
                jsonObject.put("Amount__c", quotePriceAdjustment.getAmount());
                jsonObject.put("Operator__c", quotePriceAdjustment.getOperator());
                jsonObject.put("Percent__c", quotePriceAdjustment.getPercent());
                jsonObject.put("Reason__c", quotePriceAdjustment.getReason());
                jsonObject.put("Type__c", quotePriceAdjustment.getType());
                jsonObject.put("AmountBeforeAdjustment__c", quotePriceAdjustment.getAmountBeforeAdjustment());
                jsonObject.put("AmountAfterAdjustment__c", quotePriceAdjustment.getAmountAfterAdjustment());

				jsonArray.put(jsonObject);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
				
		return jsonArray;
	}		
}
