package com.example.scchildcare;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class Single_AsyncTask extends FragmentActivity{
	
	ProgressBar progressBar;
	private static final String TAG_CENTER_DATA = "dataforcenter";
	String providerName = "";
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_bar);
        //////////////////////////////////////
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //////////////////////////////////////
        Intent anIntent = getIntent();
        HashMap<String, String> cmap = new HashMap<String, String>();
	    
	    cmap = (HashMap<String, String>)anIntent.getSerializableExtra(TAG_CENTER_DATA);
	    providerName = anIntent.getStringExtra("THE_PROVIDER");
	    System.out.println(providerName);
	    SingleItemResults searchResults = new SingleItemResults(getApplicationContext(), cmap, this, progressBar);
		searchResults.execute(providerName);
	}
	
	class SingleItemResults extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>
	{

	  String complaintSearchURL = "http://54.201.44.59:3000/providercomplaints.json?utf8=%E2%9C%93&search=";
	  String complaintFullSearchURL = null;
	  JSONParser jComplaintParser = new JSONParser();
	 
	  JSONArray complaints = null;
	  ArrayList<HashMap<String, String>> complaintList = new ArrayList<HashMap<String, String>>();
	  ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();
	  private static final String TAG_COMPLAINTTYPE = "complaint_type";
	  private static final String TAG_COMPLAINTDATE = "issueDate";
	  private static final String TAG_COMPLAINTRESOLVED = "resolved";
	  private static final String TAG_COMPLAINTS = "providercomplaints";
	  private static final String TAG_CENTER_DATA = "dataforcenter";
	  private static final String commentGetURL1 = "http://54.201.44.59:3000/providers/";
	  private static final String commentGetURL2 = "/comments.json";
	  private static final String TAG_COMMENTS = "comments";
	  private static final String TAG_PROVIDER_COMMENT_ID = "provider_id";
	  private static final String TAG_ALIAS = "user";
	  private static final String TAG_BODY = "body";
	  private static final String TAG_GET_COMMENTS = "grabbing comments";
	  boolean isConnected = false;
	  Single_AsyncTask single;
	  ProgressBar pBar;
	  JSONParser jParser = new JSONParser();
	  JSONArray comments = null;
	  
	   
    	Context aContext;
    	HashMap<String, String> theMap;
    	SingleItemResults(Context context, HashMap<String, String> aMap, Single_AsyncTask c, ProgressBar theBar)
    	{
    		aContext = context;
    		theMap = aMap;
    		single = c;
    		pBar = theBar;
    	}
    	
		protected void onPostExecute(ArrayList<HashMap<String, String>> result)
    	{	
    	if(isConnected == true){	
    	Intent anIntent = new Intent(aContext.getApplicationContext(), SingleMenuItemActivity.class);
    	anIntent.putExtra(TAG_COMPLAINTS, (Serializable)result);
    	anIntent.putExtra(TAG_CENTER_DATA, (Serializable)theMap);
    	anIntent.putExtra(TAG_GET_COMMENTS, (Serializable)commentList);
    	pBar.setVisibility(View.VISIBLE);
    	single.finish();
    	startActivity(anIntent);
    	}
    	else
		 {
			 Intent noConnect = new Intent(aContext.getApplicationContext(), ConnectionErrorActivity.class);
			 pBar.setVisibility(View.INVISIBLE);
			 single.finish();
			 startActivity(noConnect); 
		 }
    	}
    	
    	
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) 
		{
			String providerName = params[0];
			complaintFullSearchURL = complaintSearchURL + providerName;
			String complaintActualSearch = complaintFullSearchURL.replace(" ", "+");
			
			if(isURLReachable(aContext))
			{
			     isConnected = true;
				 JSONObject complaintjson = jComplaintParser
			                .getJSONFromUrl(complaintActualSearch);	
				
				 System.out.println("COMPLAINT HTTP SUCCESSFUL");
			        try {
			            // get the array of providers
			            System.out.println("CREATING THE COMPLAINTS JSON ARRAY");

			            complaints = complaintjson.getJSONArray(TAG_COMPLAINTS);
			            System.out.println("Beginning For Loop to go through array");

			            
			                for (int i = 0; i < complaints.length(); i++) {
			                    JSONObject complaint = complaints.getJSONObject(i);

			                    // store the json items in variables

			                    String complaintType = complaint.getString(TAG_COMPLAINTTYPE);
			                    String issueDate = complaint.getString(TAG_COMPLAINTDATE);
			                    String complaintResolved = complaint
			                            .getString(TAG_COMPLAINTRESOLVED);

			                    HashMap<String, String> cmap = new HashMap<String, String>();

			                    cmap.put(TAG_COMPLAINTTYPE, complaintType);
			                    cmap.put(TAG_COMPLAINTDATE, issueDate);
			                    cmap.put(TAG_COMPLAINTRESOLVED, complaintResolved);

			                    // add Hashlist to ArrayList
			                    System.out
			                            .println("Adding Tags to Map, adding map to providerList");
			                    complaintList.add(cmap);

			                }
			            
			        } catch (JSONException e) {
			            e.printStackTrace();
			        }
	///////////////////////////////////////////////////////////////////////////////		        
			    HttpClient getClient = new DefaultHttpClient();
			    HttpConnectionParams.setConnectionTimeout(getClient.getParams(), 10000); //Timeout Limit
					
    			String getURL = commentGetURL1 + providerName + commentGetURL2;
				
				JSONObject json = jParser.getJSONFromUrl(getURL);
					
					try{
						comments = json.getJSONArray(TAG_COMMENTS);
						
						for (int i = 0; i < comments.length(); i++) {
							System.out.println("Comment 1");
		                    JSONObject complaint = comments.getJSONObject(i);
		
		                    // store the json items in variables
		
		                    String alias = complaint.getString(TAG_ALIAS);
		                    String body = complaint.getString(TAG_BODY);
		                    
		
	                    HashMap<String, String> commentMap = new HashMap<String, String>();
		
		                    commentMap.put(TAG_ALIAS, alias);
		                    commentMap.put(TAG_BODY, body);
		
//		                    // add Hashlist to ArrayList
		                    System.out.println("Adding Tags to Map, adding map to commentList");
    	                    commentList.add(commentMap);
		
		                }
										
     				} catch (JSONException e) {
						e.printStackTrace();
					}
						        
			        
			        
			        
			        
			        
			        
			        
			        
			        
			        
			        
			        
			        
			        
	////////////////////////////////////////////////////////////////////////////////////			
			}
			else{
				isConnected = false;
			}
			return complaintList;
		}
		 public boolean isURLReachable(Context context) {
			    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo netInfo = cm.getActiveNetworkInfo();
			    if (netInfo != null && netInfo.isConnected()) {
			        try {
			            URL url = new URL("http://54.201.44.59");   // Change to "http://google.com" for www  test.
			            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			            urlc.setConnectTimeout(10 * 1000);          // 10 s.
			            urlc.connect();
			            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
			                Log.wtf("Connection", "Success !");
			                return true;
			            } else {
			                return false;
			            }
			        } catch (MalformedURLException e1) {
			            return false;
			        } catch (IOException e) {
			            return false;
			        }
			    }
			    return false;
			}
	}

}
