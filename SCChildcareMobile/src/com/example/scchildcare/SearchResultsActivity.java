package com.example.scchildcare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.google.android.gms.maps.model.LatLngBounds;

//import com.example.myfirstapp.trackdata.TrackData;

public class SearchResultsActivity extends ListActivity {

	// JSON node names
	//private static final String TAG_PROVIDERS = "providers";

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
	private long mLastClickTime = 0;
	public static final String SORRY_MESSAGE = "com.example.myfirstapp.SORRY";
	ArrayList<HashMap<String, String>> containingMaps = new ArrayList<HashMap<String, String>>();
	private static final LatLng SOUTH_CAROLINA = new LatLng(34.0096138,
			-81.0392966);
	private static final String TAG_CENTER_DATA = "dataforcenter";

	GoogleMap mMap;

	// ///////////////////
	String theMarker = "";
	// //////////////////


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
		//get the message from the intent
		Intent intent = getIntent();
		Bundle getProviders = intent.getExtras();
		containingMaps = (ArrayList<HashMap<String, String>>) getProviders
				.getSerializable(TAG_LIST_OF_PROVIDERS);
		// ////////////////////////////////////////////////////////////////////

		if (containingMaps.size() == 0) {
			System.out.println("No Return on Search");
			Intent sorryIntent = new Intent(this, SorryMessageActivity.class);
			this.finish();
			startActivity(sorryIntent);
		} else {
			for (int i = 0; i < containingMaps.size(); i++) {

				HashMap<String, String> map = new HashMap<String, String>();
				map = containingMaps.get(i);

				String longitude = map.get(TAG_LONGITUDE);
				String latitude = map.get(TAG_LATITUDE);
				String providerName = map.get(TAG_PROVIDERNAME);

				System.out.println(map.get(TAG_ID) + " 1  "
						+ map.get(TAG_PROVIDERNAME) + " 2 "
						+ map.get(TAG_LICENSEINFO) + " 3 "
						+ map.get(TAG_OWNERNAME) + "  4 "
						+ map.get(TAG_ADDRESS) + " 5 " + map.get(TAG_CITY)
						+ "  6 " + map.get(TAG_STATE) + " 7 "
						+ map.get(TAG_ZIPCODE) + "  8 "
						+ map.get(TAG_PHONENUMBER) + "  9 "
						+ map.get(TAG_PHONENUMBER) + " 10  "
						+ map.get(TAG_LONGITUDE) + " 11 "
						+ map.get(TAG_LATITUDE) + "  12 "
						+ map.get(TAG_CAPACITY) + " 13 "
						+ map.get(TAG_CAPACITY) + "  14 " + map.get(TAG_HOURS)
						+ " 15  " + map.get(TAG_SPECIALIST) + " 16 "
						+ map.get(TAG_SPECIALISTPHONE) + " 17 "
						+ map.get(TAG_QUALITY) + " 18 ");

				// ///////////////////////////////////////////////////////////////////
				// add Hashlist to ArrayList
				System.out
						.println("Adding Tags to Map, adding map to providerList");

				double dbl_latitude = Double.parseDouble(latitude);
				double dbl_longitude = Double.parseDouble(longitude);

				mMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				mMap.addMarker(new MarkerOptions().position(
						new LatLng(dbl_latitude, dbl_longitude)).title(
						providerName));
				// ///////////////////////////////////////////////////
				mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker aMarker) {
						theMarker = (aMarker.getTitle());
						aMarker.showInfoWindow();
						goToCenter(theMarker);
						return true;
					}

				});
				// //////////////////////////////////

			}
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			mMap.moveCamera(CameraUpdateFactory
					.newLatLngZoom(SOUTH_CAROLINA, 7));
		}

		ListAdapter adapter = new SimpleAdapter(this, containingMaps,
				R.layout.list_item, new String[] { TAG_ID, TAG_PROVIDERNAME,
						TAG_LICENSEINFO, TAG_OWNERNAME, TAG_ADDRESS, TAG_CITY,
						TAG_STATE, TAG_ZIPCODE, TAG_PHONENUMBER, TAG_LATITUDE,
						TAG_LONGITUDE, TAG_CAPACITY, TAG_HOURS, TAG_SPECIALIST,
						TAG_SPECIALISTPHONE, TAG_QUALITY }, new int[] {
						R.id.id, R.id.name, R.id.licenseInfo, R.id.ownerName,
						R.id.address, R.id.city, R.id.state, R.id.zipCode,
						R.id.phone, R.id.latitude, R.id.longitude,
						R.id.capacity, R.id.hours, R.id.specialist,
						R.id.specialistPhone, R.id.qualityLevel });

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
				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();

				// getting values from selected ListItem
				String providerID = ((TextView) view.findViewById(R.id.id))
						.getText().toString();

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
				// Intent in = new Intent(getApplicationContext(),
				// SingleMenuItemActivity.class);

				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_ID, providerID);
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
  
				Intent anIntent = new Intent(lv.getContext(), Single_AsyncTask.class);
				anIntent.putExtra(TAG_CENTER_DATA, (Serializable)map);
				anIntent.putExtra("THE_PROVIDER", providerID);
			    startActivity(anIntent);
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

	private void goToCenter(final String aString) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"Would you like more information about " + aString
								+ " ?")
				.setCancelable(false)
				.setPositiveButton("Go to child care provider page",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// ////////////////////////////////////////////////////////////////
								int i = 0;
								while (i < containingMaps.size()) {

									HashMap<String, String> map = new HashMap<String, String>();
									map = containingMaps.get(i);
									String providerName = map
											.get(TAG_PROVIDERNAME);
									String providerID = map.get(TAG_ID);
									if (providerName.equals(aString)) {
										Intent anIntent = new Intent(getApplicationContext(), Single_AsyncTask.class);
										anIntent.putExtra(TAG_CENTER_DATA, (Serializable)map);
						                anIntent.putExtra("THE_PROVIDER", providerID);
						                startActivity(anIntent);
										break;
									}
									i++;
								}

								// /////////////////////////////////////////////////////////////////
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString(MainActivity.EXTRA_MESSAGE, message);
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

}