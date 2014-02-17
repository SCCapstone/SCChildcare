package com.example.scchildcare;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.example.myfirstapp.trackdata.TrackData;

public class GPS_SearchResultsActivity extends ListActivity {
	private static final String gpsURL_1 = "http://54.201.44.59:3000/providers/gpssearch.json?utf8=%E2%9C%93&long=";
	private static final String gpsURL_2 = "&lat=";
	// private static byte[] buff = new byte[1024];
	// private static String result = null;
	private static String fullGPS_URL = null;

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

	public static final String SORRY_MESSAGE = "com.example.myfirstapp.SORRY";
	
	
	GoogleMap mMap;

	// providers JSONArray
	JSONArray providers = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_results, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the text view as the activity layout
		setContentView(R.layout.activity_search_results);

		// Hashmap for ListView
		ArrayList<HashMap<String, String>> providerList = new ArrayList<HashMap<String, String>>();

		// Simple quick fix for http exception, FIX LATER WITH ASYNCTASK*******
		ThreadPolicy tp = ThreadPolicy.LAX;
		StrictMode.setThreadPolicy(tp);

		// Get the message from the intent
		Intent intent = getIntent();
		Bundle coordinates = intent.getExtras();
		
		String param_longitude = coordinates.getString("EXTRA_LONGITUDE");
		String param_latitude = coordinates.getString("EXTRA_LATITUDE");
		System.out.println(param_latitude + ", " +param_longitude);
		fullGPS_URL = gpsURL_1+ param_longitude + gpsURL_2 + param_latitude;

		System.out.println("Beginning JSON Parse");
		JSONParser jParser = new JSONParser();

		System.out.println("Getting JSON with HTTP");
		JSONObject json = jParser.getJSONFromUrl(fullGPS_URL);
		// System.out.println(json);

		System.out.println("HTTP SUCCESSFUL");
		try {
			// get the array of providers
			System.out.println("CREATING THE PROVIDERS JSON ARRAY");

			providers = json.getJSONArray(TAG_PROVIDERS);

			System.out.println("Beginning For Loop to go through array");

			if (providers.length() == 0) {
				System.out.println("No Return on Search");
				// String sorry =
				// "We are sorry, your search did not return anything";
				// TextView textView = new TextView(this);
				// textView.setTextSize(40);
				// textView.setText(sorry);
				Intent sorryIntent = new Intent(this,
						SorryMessageActivity.class);
				// intent.putExtra(SORRY_MESSAGE, sorry);
				startActivity(sorryIntent);
			} else {
				for (int i = 0; i < providers.length(); i++) {
					JSONObject p = providers.getJSONObject(i);

					// store the json items in variables
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

					// add Hashlist to ArrayList
					System.out
							.println("Adding Tags to Map, adding map to providerList");
					providerList.add(map);
					
					
					double dbl_latitude = Double.parseDouble(latitude);
					double dbl_longitude = Double.parseDouble(longitude);
					
					double your_latitude = Double.parseDouble(param_latitude);
					double your_longitude = Double.parseDouble(param_longitude);
					
					LatLng YOUR_LOCATION = new LatLng(your_latitude, your_longitude);
					
					mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
					mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
					mMap.setMyLocationEnabled(true);

						mMap.addMarker(new MarkerOptions()
							.position(new LatLng(dbl_latitude, dbl_longitude))
							.title("Hello world"));
						
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(YOUR_LOCATION, 12));

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// FIGURE OUT HOW TO GET STRINGS TO THE SINGLE VIEW
		 ListAdapter adapter = new SimpleAdapter(this,providerList,
		 R.layout.list_item,
		 new String[]{TAG_PROVIDERNAME, TAG_LICENSEINFO, TAG_OWNERNAME,
		 TAG_ADDRESS, TAG_CITY, TAG_STATE,
		 TAG_ZIPCODE, TAG_PHONENUMBER, TAG_LATITUDE, TAG_LONGITUDE, TAG_CAPACITY, TAG_HOURS, TAG_SPECIALIST,
		 TAG_SPECIALISTPHONE, TAG_QUALITY}, new int[]
		 {R.id.name, R.id.licenseInfo, R.id.ownerName, R.id.address,
		 R.id.city, R.id.state, R.id.zipCode,
		 R.id.phone, R.id.latitude, R.id.longitude, R.id.capacity, R.id.hours,
		 R.id.specialist, R.id.specialistPhone,
		 R.id.qualityLevel});

		// Updating parsed JSON data into ListView
//		ListAdapter adapter = new SimpleAdapter(this, providerList,
//				R.layout.list_item, new String[] { TAG_PROVIDERNAME,
//						TAG_ADDRESS, TAG_CITY, TAG_STATE, TAG_ZIPCODE },
//				new int[] { R.id.name, R.id.address, R.id.city, R.id.state,
//						R.id.zipCode });

		setListAdapter(adapter);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String providerName = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String licenseInfo = ((TextView) view.findViewById(R.id.licenseInfo)).getText().toString();
				String ownerName = ((TextView) view.findViewById(R.id.ownerName)).getText().toString();
				String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
				String latitude = ((TextView) view.findViewById(R.id.latitude)).getText().toString();
				String longitude = ((TextView) view.findViewById(R.id.longitude)).getText().toString();
				String city = ((TextView) view.findViewById(R.id.city)).getText().toString();			
			    String state = ((TextView) view.findViewById(R.id.state)).getText().toString();
				String zipCode = ((TextView) view.findViewById(R.id.zipCode)).getText().toString();
				String phoneNumber = ((TextView) view.findViewById(R.id.phone)).getText().toString();
				String capacity = ((TextView) view.findViewById(R.id.capacity)).getText().toString();
				String hours = ((TextView) view.findViewById(R.id.hours)).getText().toString();
				String specialist = ((TextView) view.findViewById(R.id.specialist)).getText().toString();
				String specialistPhone = ((TextView) view.findViewById(R.id.specialistPhone)).getText().toString();
				String qualityLevel = ((TextView) view.findViewById(R.id.qualityLevel)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SingleMenuItemActivity.class);
				in.putExtra(TAG_PROVIDERNAME, providerName);
				in.putExtra(TAG_LICENSEINFO, licenseInfo);
				in.putExtra(TAG_OWNERNAME, ownerName);
				in.putExtra(TAG_ADDRESS, address);
				in.putExtra(TAG_CITY, city);
				in.putExtra(TAG_STATE, state);
				in.putExtra(TAG_ZIPCODE, zipCode);
				in.putExtra(TAG_PHONENUMBER, phoneNumber);
				in.putExtra(TAG_LATITUDE, latitude);
				in.putExtra(TAG_LONGITUDE, longitude);
				in.putExtra(TAG_CAPACITY, capacity);
				in.putExtra(TAG_HOURS, hours);
				in.putExtra(TAG_SPECIALIST, specialist);
				in.putExtra(TAG_SPECIALISTPHONE, specialistPhone);
				in.putExtra(TAG_QUALITY, qualityLevel);
				startActivity(in);
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

}