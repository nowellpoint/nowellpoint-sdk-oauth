package com.redhat.sforce.qb.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.redhat.sforce.qb.exception.SforceServiceException;
import com.redhat.sforce.qb.service.SforceService;

public class SforceServiceImpl implements Serializable,  SforceService {		

	private static final long serialVersionUID = 6506272900287022663L;
	private static String API_VERSION = null;
	private static String INSTANCE_URL = null;
	
	private SforceServiceImpl() {
		try {
			init();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException, UnsupportedEncodingException {
		InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("quotebuilder.properties");		
        Properties properties = new Properties();
		properties.load(inStream);

		API_VERSION = properties.getProperty("api.version");
		INSTANCE_URL = properties.getProperty("instance.url");
	}	
	
	@Override
	public JSONArray read(String accessToken, String query) {
		String url = INSTANCE_URL + "/services/data/" + API_VERSION + "/query";
		
		NameValuePair[] params = new NameValuePair[1];
		params[0] = new NameValuePair("q", query);
		
		GetMethod getMethod = new GetMethod(url);
		getMethod.setRequestHeader("Authorization", "OAuth " + accessToken);
		getMethod.setRequestHeader("Content-Type", "application/json");
		getMethod.setQueryString(params);
		
		JSONArray queryResult = null;					
		try {
			
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getMethod );
			System.out.println(getMethod.getStatusCode());
			if (getMethod.getStatusCode() == HttpStatus.SC_OK) {
				try {
					JSONObject response = new JSONObject(new JSONTokener(new InputStreamReader(getMethod.getResponseBodyAsStream())));
					//System.out.println("Query response: " + response.toString(2));

					queryResult = response.getJSONArray("records");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		
		return queryResult;
	}
	
	@Override
	public String create(String accessToken, String sobject, JSONObject jsonObject) throws SforceServiceException {
		String url = INSTANCE_URL + "/services/data/" + API_VERSION + "/sobjects/" + sobject;
		
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Authorization", "OAuth " + accessToken);
		postMethod.setRequestHeader("Content-type", "application/json");
		
		String id = null;	
		try {
			postMethod.setRequestEntity(new StringRequestEntity(jsonObject.toString(), "application/json", null));
			
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postMethod);
			
			if (postMethod.getStatusCode() == HttpStatus.SC_CREATED) {
				JSONObject response = new JSONObject(new JSONTokener(new InputStreamReader(postMethod.getResponseBodyAsStream())));
				System.out.println("Create response: " + response.toString(2));

				if (response.getBoolean("success")) {
					System.out.println("created: " + response.getString("id"));
					id = response.getString("id");
				}

			} else {
				throw new SforceServiceException(parseErrorResponse(postMethod.getResponseBodyAsStream()));									
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block					
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		
		return id;
	}

	@Override
	public void delete(String accessToken, String sobject, String id) {
		String url = INSTANCE_URL + "/services/data/" + API_VERSION + "/sobjects/" + sobject + "/" + id;
				
		DeleteMethod deleteMethod = new DeleteMethod(url);
		deleteMethod.setRequestHeader("Authorization", "OAuth " + accessToken);
		deleteMethod.setRequestHeader("Content-type", "application/json");

		try {
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(deleteMethod);
			System.out.println(deleteMethod.getStatusText());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			deleteMethod.releaseConnection();
		}
		
	}

	@Override
	public void update(String accessToken, String sobject, String id, JSONObject jsonObject) throws SforceServiceException {
		String url = INSTANCE_URL + "/services/data/" + API_VERSION + "/sobjects/" + sobject + "/" + id  + "?_HttpMethod=PATCH";
		
		PostMethod postMethod = new PostMethod(url);	
		postMethod.setRequestHeader("Authorization", "OAuth " + accessToken);
		postMethod.setRequestHeader("Content-type", "application/json");
		System.out.println(jsonObject.toString());
		try {								
			postMethod.setRequestEntity(new StringRequestEntity(jsonObject.toString(), "application/json", null));
			
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postMethod);
			System.out.println("update status code: " + postMethod.getStatusCode());
			
			if (postMethod.getStatusCode() == 400) {				
				throw new SforceServiceException(parseErrorResponse(postMethod.getResponseBodyAsStream()));
			} else {
				
				
                //JSONObject response = new JSONObject(new JSONTokener(new InputStreamReader(postMethod.getResponseBodyAsStream())));
				
				//System.out.println("Update response: " + response.toString(2));

				//if (response.getBoolean("success")) {
				//	System.out.println("updated: " + response.getString("id"));
				//}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		
	}
	
	private String parseErrorResponse(InputStream is) {
		JSONArray value;
		try {
			value = (JSONArray) new JSONTokener(new InputStreamReader(is)).nextValue();
			
			JSONObject object = (JSONObject) value.get(0);
			String errorCode = object.getString("errorCode");
			String errorMessage = null;
			if (errorCode != null) {
				errorMessage = object.getString("message");
			}
			
			return errorCode + ": " + errorMessage;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * if (resp.getStatusLine().getStatusCode() == 400)
{
	JSONArray value = (JSONArray)new JSONTokener(result).nextValue();
	JSONObject object = (JSONObject)value.get(0);
	String errorCode = object.getString("errorCode");
	if (errorCode != null)
	{
   		errorMsg = object.getString("message");
   		showDialog(1);
   		return;
	}
}

"fields" : [ "BillingState" ],
"message" : "Billing State is required",
"errorCode" : "FIELD_CUSTOM_VALIDATION_EXCEPTION"


	 */
}