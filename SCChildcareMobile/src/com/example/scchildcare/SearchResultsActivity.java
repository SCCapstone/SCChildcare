


package com.example.scchildcare;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.app.NavUtils;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.google.android.gms.maps.model.LatLngBounds;


//import com.example.myfirstapp.trackdata.TrackData;

public class SearchResultsActivity extends ListActivity {

	// JSON node names
	private static final String TAG_PROVIDERS = "providers";
	private static final String TAG_ID = "id";
	private static final String TAG_PROVIDERNAME = "providerName";
	private static final String TAG_LICENSEINFO = "licenseInfo";
	private static final String TAG_OWNERNAME = "ownerName";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_CITY = "city";
	private static final String TAG_STATE = "state";
	private static final String TAG_ZIPCODE = "zipCode";
	private static final String TAG_PHONENUMBER = "phoneNumber";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_CAPACITY = "capacity";
	private static final String TAG_HOURS = "hours";
	private static final String TAG_SPECIALIST = "specialist";
	private static final String TAG_SPECIALISTPHONE = "specialistPhone";
	private static final String TAG_QUALITY = "qualityLevel";
	private static final String TAG_LIST_OF_PROVIDERS = "pList";
	String message;

	public static final String SORRY_MESSAGE = "com.example.myfirstapp.SORRY";

	private static final LatLng SOUTH_CAROLINA = new LatLng(34.0096138,	-81.0392966); 		 		

	GoogleMap mMap;
	
	/////////////////////
	Marker marker_1;
	////////////////////

	// providers JSONArray
	JSONArray providers = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the text view as the activity layout
		setContentView(R.layout.activity_search_results);

		// Hashmap for ListView

		ArrayList<HashMap<String, String>> containingMaps = new ArrayList<HashMap<String, String>>();
		
         //////////////////////////////////////////////////////////////////////////////////////////			
		Intent intent = getIntent();
		Bundle getProviders = intent.getExtras();
	    containingMaps = (ArrayList<HashMap<String, String>>) getProviders.getSerializable(TAG_LIST_OF_PROVIDERS);
		//////////////////////////////////////////////////////////////////////


		ThreadPolicy tp = ThreadPolicy.LAX;
		StrictMode.setThreadPolicy(tp);	    
	    
			if (containingMaps.size() == 0) {
				System.out.println("No Return on Search");
				Intent sorryIntent = new Intent(this,
						SorryMessageActivity.class);
				this.finish();
				startActivity(sorryIntent);
			} else {
				for (int i = 0; i < containingMaps.size(); i++) {
			
					HashMap<String, String> map = new HashMap<String, String>();
					map = containingMaps.get(i);
					
					String longitude = map.get(TAG_LONGITUDE);
					String latitude =  map.get(TAG_LATITUDE);
					String providerName = map.get(TAG_PROVIDERNAME);
					
					//////////////////////////////////////////////////////////////////////////////////
					
					System.out.println(map.get(TAG_ID) + " 1  " + map.get(TAG_PROVIDERNAME) + " 2 " + 	map.get(TAG_LICENSEINFO)
							+ " 3 " + map.get(TAG_OWNERNAME) + "  4 " + map.get(TAG_ADDRESS) + " 5 " + 	map.get(TAG_CITY)
							+ "  6 " + map.get(TAG_STATE) + " 7 " +map.get(TAG_ZIPCODE) + "  8 "+ map.get(TAG_PHONENUMBER) + "  9 "
							+ map.get(TAG_PHONENUMBER) + " 10  " + map.get(TAG_LONGITUDE)+  " 11 " + map.get(TAG_LATITUDE)
							+ "  12 " + map.get(TAG_CAPACITY) + " 13 " + map.get(TAG_CAPACITY) + "  14 "+ map.get(TAG_HOURS) 
							+ " 15  "+	map.get(TAG_SPECIALIST) + " 16 "+ map.get(TAG_SPECIALISTPHONE) + " 17 " + map.get(TAG_QUALITY) + " 18 ");
						
					/////////////////////////////////////////////////////////////////////
					// add Hashlist to ArrayList
					System.out
							.println("Adding Tags to Map, adding map to providerList");

					double dbl_latitude = Double.parseDouble(latitude);
					double dbl_longitude = Double.parseDouble(longitude);

					mMap = ((MapFragment) getFragmentManager()
							.findFragmentById(R.id.map)).getMap();


                       mMap.addMarker(new MarkerOptions().position(
                        new LatLng(dbl_latitude, dbl_longitude)).title(
                        providerName));
                       
				}
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						SOUTH_CAROLINA, 7));
			}
	
			
		ListAdapter adapter = new SimpleAdapter(this, containingMaps,
				R.layout.list_item, new String[] { TAG_PROVIDERNAME,
						TAG_LICENSEINFO, TAG_OWNERNAME, TAG_ADDRESS, TAG_CITY,
						TAG_STATE, TAG_ZIPCODE, TAG_PHONENUMBER, TAG_LATITUDE,
						TAG_LONGITUDE, TAG_CAPACITY, TAG_HOURS, TAG_SPECIALIST, TAG_SPECIALISTPHONE,
						TAG_QUALITY }, new int[] { R.id.name, R.id.licenseInfo,
						R.id.ownerName, R.id.address, R.id.city, R.id.state,
						R.id.zipCode, R.id.phone, R.id.latitude,
						R.id.longitude, R.id.capacity, R.id.hours, R.id.specialist, R.id.specialistPhone,
						R.id.qualityLevel });

		// Updating parsed JSON data into ListView
		// ListAdapter adapter = new SimpleAdapter(this, providerList,
		// R.layout.list_item, new String[] { TAG_PROVIDERNAME,
		// TAG_ADDRESS, TAG_CITY, TAG_STATE, TAG_ZIPCODE },
		// new int[] { R.id.name, R.id.address, R.id.city, R.id.state,
		// R.id.zipCode });

		setListAdapter(adapter);

		final ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String providerName = ((TextView) view.findViewById(R.id.name))
						.getText().toString();
				String licenseInfo = ((TextView) view
						.findViewById(R.id.licenseInfo)).getText().toString();
				String ownerName = ((TextView) view
						.findViewById(R.id.ownerName)).getText().toString();
				String address = ((TextView) view.findViewById(R.id.address))
						.getText().toString();
				String city = ((TextView) view.findViewById(R.id.city))
						.getText().toString();
				String state = ((TextView) view.findViewById(R.id.state))
						.getText().toString();
				String zipCode = ((TextView) view.findViewById(R.id.zipCode))
						.getText().toString();
				String phoneNumber = ((TextView) view.findViewById(R.id.phone))
						.getText().toString();
				String latitude = ((TextView) view.findViewById(R.id.latitude))
						.getText().toString();
				String longitude = ((TextView) view
						.findViewById(R.id.longitude)).getText().toString();
				String capacity = ((TextView) view.findViewById(R.id.capacity))
						.getText().toString();
				String hours = ((TextView) view.findViewById(R.id.hours))
						.getText().toString();
				
				String specialist = ((TextView) view
						.findViewById(R.id.specialist)).getText().toString();
				String specialistPhone = ((TextView) view
						.findViewById(R.id.specialistPhone)).getText()
						.toString();
				String qualityLevel = ((TextView) view
						.findViewById(R.id.qualityLevel)).getText().toString();

				// Starting new intent
			//	Intent in = new Intent(getApplicationContext(),
				//		SingleMenuItemActivity.class);
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put(TAG_PROVIDERNAME, providerName);
				map.put(TAG_LICENSEINFO, licenseInfo);
				map.put(TAG_OWNERNAME, ownerName);
				map.put(TAG_ADDRESS, address);
				map.put(TAG_CITY, city);
				map.put(TAG_STATE, state);
				map.put(TAG_ZIPCODE, zipCode);
				map.put(TAG_PHONENUMBER, phoneNumber);
				map.put(TAG_LATITUDE, latitude);
				map.put(TAG_LONGITUDE, longitude);
				map.put(TAG_CAPACITY, capacity);
				map.put(TAG_HOURS, hours);
				map.put(TAG_SPECIALIST, specialist);
				map.put(TAG_SPECIALISTPHONE, specialistPhone);
				map.put(TAG_QUALITY, qualityLevel);
				
				SingleItemResults singleItem = new SingleItemResults(lv.getContext(), map);
				singleItem.execute();
				
				//tartActivity(in);
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);

	}
	

	public void onSaveInstanceState(Bundle savedInstanceState) {
	    savedInstanceState.putString(MainActivity.EXTRA_MESSAGE, message);
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	   class SingleItemResults extends AsyncTask<String, String, String>
		{

		  String complaintSearchURL = "http://54.201.44.59:3000/providercomplaints.json?utf8=%E2%9C%93&search=";
		  String complaintFullSearchURL = null;
		   
	    	Context aContext;
	    	HashMap<String, String> theMap;
	    	SingleItemResults(Context context, HashMap<String, String> aMap)
	    	{
	    		aContext = context;
	    		theMap = aMap;
	    	}
	    	
	    	
			@Override
			protected String doInBackground(String... params) 
			{
				if(isURLReachable(aContext))
				{
				
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				}
				return null;
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