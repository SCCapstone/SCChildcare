package com.example.scchildcare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class JSONParser extends Activity{
	
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";	
	
	//constructor
	public JSONParser() {
		// TODO Auto-generated constructor stub
	}

public JSONObject getJSONFromUrl(String url) {
	// Making HTTP request
	System.out.println(url);
	
	// defaultHttpClient
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpGet httpGet = new HttpGet(url);
    //httpGet.setHeader("Accept", "application/json");
    //httpGet.setHeader("Content-type", "application/json");

    System.out.println("Making HTTP Request");
    
    try {

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
       
        is = httpEntity.getContent();          
       // System.out.println("Input Stream: " + is);

    } catch (UnsupportedEncodingException e) {
    	//System.out.println("UnsupportedEncodingException");
    	Intent errorIntent = new Intent(this, ConnectionErrorActivity.class);
    	startActivity(errorIntent);
    	System.out.println("Sent to error page");
        e.printStackTrace();
    } catch (ClientProtocolException e) {
    	//System.out.println("ClientProtocolException");
    	Intent errorIntent = new Intent(this, ConnectionErrorActivity.class);
    	startActivity(errorIntent);
    	System.out.println("Sent to error page");
        e.printStackTrace();
    } catch (IOException e) {
    	System.out.println("IOException");
    	Intent errorIntent = new Intent(this, ConnectionErrorActivity.class);
    	startActivity(errorIntent);
    	System.out.println("Sent to error page");
        e.printStackTrace();
        
    }
    

    
    try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        //System.out.println("String Builder: " + sb);
        is.close();
        json = sb.toString();
    } catch (Exception e) {
        Log.e("Buffer Error", "Error converting result " + e.toString());
    }

    // try parse the string to a JSON object
    try {
        jObj = new JSONObject(json);
       // System.out.println(jObj);
    } catch (JSONException e) {
        Log.e("JSON Parser", "Error parsing data " + e.toString());
    }

    // return JSON String
    return jObj;
    
    
}
}
