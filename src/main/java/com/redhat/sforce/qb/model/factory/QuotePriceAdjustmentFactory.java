package com.redhat.sforce.qb.model.factory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.redhat.sforce.qb.model.QuotePriceAdjustment;
import com.redhat.sforce.qb.util.JSONObjectWrapper;

public class QuotePriceAdjustmentFactory {

	public static List<QuotePriceAdjustment> deserialize(JSONArray jsonArray)
			throws JSONException, ParseException {
		List<QuotePriceAdjustment> quotePriceAdjustmentList = new ArrayList<QuotePriceAdjustment>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObjectWrapper wrapper = new JSONObjectWrapper(
					jsonArray.getJSONObject(i));

			QuotePriceAdjustment quotePriceAdjustment = new QuotePriceAdjustment();
			quotePriceAdjustment.setId(wrapper.getId());
			quotePriceAdjustment.setCreatedById(wrapper.getString("CreatedBy",
					"Id"));
			quotePriceAdjustment.setCreatedByName(wrapper.getString(
					"CreatedBy", "Name"));
			quotePriceAdjustment.setCreatedDate(wrapper
					.getDateTime("CreatedDate"));
			quotePriceAdjustment.setCurrencyIsoCode(wrapper
					.getString("CurrencyIsoCode"));
			quotePriceAdjustment.setLastModifiedById(wrapper.getString(
					"LastModifiedBy", "Id"));
			quotePriceAdjustment.setLastModifiedByName(wrapper.getString(
					"LastModifiedBy", "Name"));
			quotePriceAdjustment.setLastModifiedDate(wrapper
					.getDateTime("LastModifiedDate"));
			quotePriceAdjustment.setAdjustmentAmount(wrapper
					.getDouble("AdjustmentAmount__c"));
			quotePriceAdjustment.setOperator(wrapper.getString("Operator__c"));
			quotePriceAdjustment.setPercent(wrapper.getDouble("Percent__c"));
			quotePriceAdjustment.setReason(wrapper.getString("Reason__c"));
			quotePriceAdjustment.setType(wrapper.getString("Type__c"));
			quotePriceAdjustment
					.setAppliesTo(wrapper.getString("AppliesTo__c"));

			quotePriceAdjustmentList.add(quotePriceAdjustment);
		}

		return quotePriceAdjustmentList;
	}

	public static JSONArray serialize(
			List<QuotePriceAdjustment> quotePriceAdjustmentList) {
		JSONArray jsonArray = new JSONArray();

		for (QuotePriceAdjustment quotePriceAdjustment : quotePriceAdjustmentList) {

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("Id", quotePriceAdjustment.getId());
				jsonObject.put("QuoteId__c", quotePriceAdjustment.getQuoteId());
				jsonObject.put("AdjustmentAmount__c",
						quotePriceAdjustment.getAdjustmentAmount());
				jsonObject.put("Operator__c",
						quotePriceAdjustment.getOperator());
				jsonObject.put("Percent__c", quotePriceAdjustment.getPercent());
				jsonObject.put("Reason__c", quotePriceAdjustment.getReason());
				jsonObject.put("Type__c", quotePriceAdjustment.getType());
				jsonObject.put("AppliesTo__C",
						quotePriceAdjustment.getAppliesTo());

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
