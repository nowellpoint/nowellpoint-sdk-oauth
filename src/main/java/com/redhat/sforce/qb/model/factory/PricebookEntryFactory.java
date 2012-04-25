package com.redhat.sforce.qb.model.factory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.redhat.sforce.qb.model.OpportunityLineItem;
import com.redhat.sforce.qb.model.PricebookEntry;
import com.redhat.sforce.qb.util.JSONObjectWrapper;

public class PricebookEntryFactory {

	public static List<PricebookEntry> deserialize(JSONArray jsonArray) throws JSONException, ParseException {
		List<PricebookEntry> pricebookEntryList = new ArrayList<PricebookEntry>();

		for (int i = 0; i < jsonArray.length(); i++) {
			PricebookEntry pricebookEntry = deserialize(jsonArray.getJSONObject(i));
			pricebookEntryList.add(pricebookEntry);
		}

		return pricebookEntryList;
	}

	public static PricebookEntry deserialize(JSONObject jsonObject) {
		JSONObjectWrapper wrapper = new JSONObjectWrapper(jsonObject);

		PricebookEntry pricebookEntry = null;
		try {
			pricebookEntry = new PricebookEntry();
			pricebookEntry.setId(wrapper.getId());
			pricebookEntry.setCurrencyIsoCode(wrapper.getString("CurrencyIsoCode"));
			pricebookEntry.setUnitPrice(wrapper.getDouble("UnitPrice"));
			pricebookEntry.setProduct(ProductFactory.parseProduct(wrapper.getJSONObject("Product2")));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pricebookEntry;
	}

	public static JSONObject serialize(OpportunityLineItem opportunityLineItem) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Id", opportunityLineItem.getPricebookEntryId());
			jsonObject.put("Product2", ProductFactory.serialize(opportunityLineItem));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return jsonObject;
	}
}