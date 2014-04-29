package com.mobile.scchildcare;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.scchildcare.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

public class GPS_Search_AsyncTask extends FragmentActivity{
	
	  ProgressBar progressBar;
	  String param_latitude = "";
	  String param_longitude = "";
	  
	  public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.p_bar);
          /////////////////////////////////////////////
          
          ////////////////////////////////////////////
          Intent anIntent = getIntent();
          Bundle getLocation = anIntent.getExtras();
          param_longitude = getLocation.getString("EXTRA_LONGITUDE");
	      param_latitude = getLocation.getString("EXTRA_LATITUDE");
	      progressBar = (ProgressBar) findViewById(R.id.progressBar1);
          GetGPSResults gpsResults = new GetGPSResults(getApplicationContext(), this, progressBar);
		  gpsResults.execute(param_latitude, param_longitude);
          
	  }
	  
	  class GetGPSResults extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>
	  {
	  	 String gpsURL_1 = "http://54.201.44.59:3000/providers/gpssearch.json?utf8=%E2%9C%93&long=";
	  	 String gpsURL_2 = "&lat=";
	  	 String TAG_PROVIDERS = "providers";
	  	 String TAG_ID = "id";
	  	 String TAG_PROVIDERNAME = "providerName";
	  	 String TAG_LICENSEINFO = "licenseInfo";
	  	 String TAG_OWNERNAME = "ownerName";
	  	 String TAG_ADDRESS = "address";
	  	 String TAG_CITY = "city";
	  	 String TAG_STATE = "state";
	  	 String TAG_ZIPCODE = "zipCode";
	  	 String TAG_PHONENUMBER = "phoneNumber";
	  	 String TAG_LONGITUDE = "longitude";
	  	 String TAG_LATITUDE = "latitude";
	  	 String TAG_CAPACITY = "capacity";
	  	 String TAG_HOURS = "hours";
	  	 String TAG_SPECIALIST = "specialist";
	  	 String TAG_SPECIALISTPHONE = "specialistPhone";
	  	 String TAG_QUALITY = "qualityLevel";
	       String TAG_LIST_OF_PROVIDERS = "pList";
	       JSONParser jParser = new JSONParser();
	       String latit;
	       String longit;
	  	 JSONArray providers = null;
	  	 String fullGPS_URL = null;
	  	 boolean isConnected = false;
	  //	 ProgressBar progBar;
	   //  private ProgressBar progressBar;
	     ArrayList<HashMap<String, String>> storeData = new ArrayList<HashMap<String, String>>();
	  	// JSON node names
	     
	     ProgressBar pBar;
	     
	     GPS_Search_AsyncTask GPS_AsyncTask;

	  	 Context aContext;
	     private GetGPSResults(Context context, GPS_Search_AsyncTask a, ProgressBar theBar) 
	      {
	         aContext = context;
	         GPS_AsyncTask = a;
	         pBar = theBar;
	      }

	  	protected void onPostExecute(ArrayList<HashMap<String, String>> result)
	  	{	
	  		
	  	 if(isConnected == true){	
	  	 Intent GPSSearch = new Intent(aContext.getApplicationContext(), GPS_SearchResultsActivity.class);
	  	 GPSSearch.putExtra(TAG_LIST_OF_PROVIDERS, (Serializable)result);
	  	 Bundle coordinates = new Bundle();
	  	 coordinates.putString("EXTRA_LATITUDE", latit);
	  	 coordinates.putString("EXTRA_LONGITUDE", longit);
	  	 GPSSearch.putExtras(coordinates);
	     pBar.setVisibility(View.INVISIBLE);
	  	 GPS_AsyncTask.finish();
	  	 startActivity(GPSSearch);
	  	 }
	  	 else{
	  		 Intent noConnect = new Intent(aContext.getApplicationContext(), ConnectionErrorActivity.class);
	  		 pBar.setVisibility(View.INVISIBLE);
	  		 GPS_AsyncTask.finish();
	  		 startActivity(noConnect);
	  	 }
	  	}
	  	protected ArrayList<HashMap<String, String>> doInBackground(String... args) 
	  	{
	 
	  		String param_latitude = args[0];
	  		String param_longitude = args[1];
	  		latit = args[0];
	  		longit = args[1];
	  		System.out.println(param_longitude + " " + param_latitude + " in do background");
	  	       fullGPS_URL = gpsURL_1 + param_longitude + gpsURL_2 + param_latitude;
	  	        
	  				//System.out.println("Beginning JSON Parse");
	  	       if(isURLReachable(aContext) == true)
	  			{
	  	    	 isConnected = true;
	  	    	 System.out.println( " server is running ");   
	  				
	  	       if(!param_longitude.isEmpty() && !param_latitude.isEmpty())
	  			{
	  	    	//	ThreadPolicy tp = ThreadPolicy.LAX;
	  	    		//StrictMode.setThreadPolicy(tp);
	  				//System.out.println("Getting JSON with HTTP");
	  			JSONObject json = jParser.getJSONFromUrl(fullGPS_URL);
	  			if(json.length() > 0){
	  				System.out.println( "non null json object");
	  			}
	  			try 
	  			{
	  				providers = json.getJSONArray(TAG_PROVIDERS);
	  	//			System.out.println(json.toString());
	  				System.out.println(" providers length " + providers.length());
	  				if (providers.length() > 0) 
	  				{
	  					System.out.println(" providers list is not empty ");
	  					for (int i = 0; i < providers.length(); i++) 
	  					{
	  						JSONObject p = providers.getJSONObject(i);
	  						String id = p.getString(TAG_ID);
	  						String providerName = p.getString(TAG_PROVIDERNAME);
	  						String licenseInfo = p.getString(TAG_LICENSEINFO);
	  						String ownerName = p.getString(TAG_OWNERNAME);
	  						String address = p.getString(TAG_ADDRESS);
	  						String city = p.getString(TAG_CITY);
	  						String state = p.getString(TAG_STATE);
	  						String zipCode = p.getString(TAG_ZIPCODE);
	  						String phoneNumber = p.getString(TAG_PHONENUMBER);
	  						String longitude = p.getString(TAG_LONGITUDE);
	  						String latitude = p.getString(TAG_LATITUDE);
	  						String capacity = p.getString(TAG_CAPACITY);
	  						String hours = p.getString(TAG_HOURS);
	  						String specialist = p.getString(TAG_SPECIALIST);
	  						String specialistPhone = p.getString(TAG_SPECIALISTPHONE);
	  						String qualityLevel = p.getString(TAG_QUALITY);

	  						HashMap<String, String> map = new HashMap<String, String>();
	  						
	  						map.put(TAG_ID, id);
	  						map.put(TAG_PROVIDERNAME, providerName);
	  						map.put(TAG_LICENSEINFO, licenseInfo);
	  						map.put(TAG_OWNERNAME, ownerName);
	  						map.put(TAG_ADDRESS, address);
	  						map.put(TAG_CITY, city);
	  						map.put(TAG_STATE, state);
	  						map.put(TAG_ZIPCODE, zipCode);
	  						map.put(TAG_PHONENUMBER, phoneNumber);
	  						map.put(TAG_LONGITUDE, longitude);
	  						map.put(TAG_LATITUDE, latitude);
	  						map.put(TAG_CAPACITY, capacity);
	  						map.put(TAG_HOURS, hours);
	  						map.put(TAG_SPECIALIST, specialist);
	  						map.put(TAG_SPECIALISTPHONE, specialistPhone);
	  						map.put(TAG_QUALITY, qualityLevel);
	  						
	  						storeData.add(map);
	  					
	  					}
	  				}	
	  			} catch (JSONException e)
	  			{
	  				e.printStackTrace();		
	  		}  
	  			}
	  	}
	  	       else{
	  	    	   isConnected = false;
	  	       }
	  			return storeData;
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
	  	 /*
	  	
	  	public static boolean isReachableByPing(String host) {
	  	    try{
	  	        String cmd = "";

	  	        if(System.getProperty("os.name").startsWith("Windows"))
	  	            cmd = "ping -n 1 " + host; // For Windows
	  	        else
	  	            cmd = "ping -c 1 " + host; // For Linux and OSX

	  	        Process myProcess = Runtime.getRuntime().exec(cmd);
	  	        myProcess.waitFor();

	  	        return myProcess.exitValue() == 0;
	  	    } catch( Exception e ) {
	  	        e.printStackTrace();
	  	        return false;
	  	    }
	  	}
	  	
	  	 
	  	 */
	  	 
	  	 
	  }

	  
	  

}
