package com.app.laundry.json;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static JSONArray jArray = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url) {

		json=null;
		jObj = null;
		jArray = null;
		if(url!=null){
		 HttpClient client = new DefaultHttpClient();
		    // Perform a GET request for a JSON list
		 
		    HttpUriRequest request = new HttpGet(url);
		    
		    // Get the response that sends back
		    HttpResponse response = null;
		    try {
		        response = client.execute(request);
		    }
		    catch(HttpHostConnectException e2){
		    	 e2.printStackTrace();
		    }
		    catch (ClientProtocolException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }
		    if (response!=null) {
		    HttpEntity entity = response.getEntity();

		    try {
		        json = EntityUtils.toString(entity);
		    } catch (ParseException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }


		    try {
		        jObj = new JSONObject(json);
		        Log.v("jObj success---------------", "" + jObj);
		    } catch (JSONException e) {
		    	Log.v("jObj---------------", "" + jObj);
		        Log.v("JSON Parser", "Error parsing data " + e.toString());
		    }
		    }
		}
		    // return JSON String
		    return jObj;

	}
	
	
	public JSONArray getJSONArrayFromUrl(String url) {

		json=null;
		jObj = null;
		jArray = null;
		if(url!=null){
		 HttpClient client = new DefaultHttpClient();
		    // Perform a GET request for a JSON list
		 
		    HttpUriRequest request = new HttpGet(url);
		    
		    // Get the response that sends back
		    HttpResponse response = null;
		    try {
		        response = client.execute(request);
		    }
		    catch(HttpHostConnectException e2){
		    	 e2.printStackTrace();
		    }
		    catch (ClientProtocolException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }
		    if (response!=null) {
		    HttpEntity entity = response.getEntity();

		    try {
		        json = EntityUtils.toString(entity);
		    } catch (ParseException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }


		    try {
		    	jArray = new JSONArray(json);
		        Log.v("jObj success---------------", "" + jArray);
		    } catch (JSONException e) {
		    	Log.v("jObj---------------", "" + jArray);
		        Log.v("JSON Parser", "Error parsing data " + e.toString());
		    }
		    }
		}
		    // return JSON String
		    return jArray;

	}
	
	public String getBooleanResponceFromUrl(String url) {

		 json = "false";
		jObj = null;
		jArray = null;
		if(url!=null){
		 HttpClient client = new DefaultHttpClient();
		    // Perform a GET request for a JSON list
		 
		    HttpUriRequest request = new HttpGet(url);
		    
		    // Get the response that sends back
		    HttpResponse response = null;
		    try {
		        response = client.execute(request);
		    }
		    catch(HttpHostConnectException e2){
		    	 e2.printStackTrace();
		    }
		    catch (ClientProtocolException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }
		    if (response!=null) {
		    HttpEntity entity = response.getEntity();

		    try {
		        json = EntityUtils.toString(entity);
		    } catch (ParseException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }

		    }
		    }
		    return json;

	}
}
