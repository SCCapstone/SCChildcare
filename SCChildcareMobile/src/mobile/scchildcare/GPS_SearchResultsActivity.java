package mobile.scchildcare;

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

import mobile.scchildcare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.LatLngBounds;

//import com.example.myfirstapp.trackdata.TrackData;

public class GPS_SearchResultsActivity extends ListActivity {

	// JSON node names
	// private static final String TAG_PROVIDERS = "providers";
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
	private static final String TAG_CENTER_DATA = "dataforcenter";
	public static final String SORRY_MESSAGE = "com.example.myfirstapp.SORRY";
	ArrayList<HashMap<String, String>> containingMaps = new ArrayList<HashMap<String, String>>();
	private long mLastClickTime = 0;
	String theMarker = "";
	double yourlat;
	double yourlong;
	String param_long;
	String param_lat;

	GoogleMap mMap;

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
	/**
	 * This class gets the search results for the GPS search, in the form of a JSONObject Array, 
	 * It then builds a listview and a map plotting all available points. 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the text view as the activity layout
		setContentView(R.layout.activity_search_results);

		// Hashmap for ListView

		// ///////////////////////////////////////////////////////////////////
		Intent intent = getIntent();
		Bundle getProviders = intent.getExtras();
		containingMaps = (ArrayList<HashMap<String, String>>) getProviders
				.getSerializable(TAG_LIST_OF_PROVIDERS);
		String param_longitude = getProviders.getString("EXTRA_LONGITUDE");
		String param_latitude = getProviders.getString("EXTRA_LATITUDE");
		param_long = param_longitude;
		param_lat = param_latitude;
		System.out.println(param_longitude + "  this is longitude "
				+ param_latitude + " this is latitude");
		// ///////////////////////////////////////////////////////////////////////

		if (containingMaps.size() == 0) {
			System.out.println("No Return on Search");

			Intent sorryIntent = new Intent(this, SorryMessageActivity.class);
			this.finish();
			startActivity(sorryIntent);
		} else {
			for (int i = 0; i < containingMaps.size(); i++) {
				// JSONObject p = providers.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map = containingMaps.get(i);
				// store the json items in variables

				String longitude = map.get(TAG_LONGITUDE);
				String latitude = map.get(TAG_LATITUDE);
				String providerName = map.get(TAG_PROVIDERNAME);

				System.out
						.println("Adding Tags to Map, adding map to providerList");

				double dbl_latitude = Double.parseDouble(latitude);
				double dbl_longitude = Double.parseDouble(longitude);

				double your_latitude = Double.parseDouble(param_latitude);
				double your_longitude = Double.parseDouble(param_longitude);

				yourlat = your_latitude;
				yourlong = your_longitude;

				System.out.println(your_latitude + " " + your_longitude);

				// Plotting users current location
				LatLng YOUR_LOCATION = new LatLng(your_latitude, your_longitude);

				mMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				mMap.setMyLocationEnabled(true);

				mMap.addMarker(new MarkerOptions().position(
						new LatLng(dbl_latitude, dbl_longitude)).title(
						providerName));

				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						YOUR_LOCATION, 12));

				mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker aMarker) {
						theMarker = (aMarker.getTitle());
						aMarker.showInfoWindow();
						goToCenter(theMarker);
						return true;
					}

				});

			}
		}

		// FIGURE OUT HOW TO GET STRINGS TO THE SINGLE VIEW
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
				String latitude = ((TextView) view.findViewById(R.id.latitude))
						.getText().toString();
				String longitude = ((TextView) view
						.findViewById(R.id.longitude)).getText().toString();
				String city = ((TextView) view.findViewById(R.id.city))
						.getText().toString();
				String state = ((TextView) view.findViewById(R.id.state))
						.getText().toString();
				String zipCode = ((TextView) view.findViewById(R.id.zipCode))
						.getText().toString();
				String phoneNumber = ((TextView) view.findViewById(R.id.phone))
						.getText().toString();
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

				Intent anIntent = new Intent(lv.getContext(),
						Single_AsyncTask.class);
				anIntent.putExtra(TAG_CENTER_DATA, (Serializable) map);
				anIntent.putExtra("THE_PROVIDER", providerID);
				anIntent.putExtra("YOURLAT", param_lat);
				anIntent.putExtra("YOURLONG", param_long);
				startActivity(anIntent);
			}
		});

	}

	/**
	 * By clicking a single map point, the users will be asked if the would like
	 * to go to the center
	 * 
	 * @param aString
	 */
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
									String providerID = map.get(TAG_ID);
									String providerName = map
											.get(TAG_PROVIDERNAME);
									if (providerName.equals(aString)) {
										Intent anIntent = new Intent(
												getApplicationContext(),
												Single_AsyncTask.class);
										anIntent.putExtra(TAG_CENTER_DATA,
												(Serializable) map);
										anIntent.putExtra("THE_PROVIDER",
												providerID);
										anIntent.putExtra("YOURLAT", param_lat);
										anIntent.putExtra("YOURLONG",
												param_long);
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