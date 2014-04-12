package com.example.scchildcare;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SingleMenuItemActivity extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	Location mCurrentLocation;
	LocationClient mLocationClient;
	double currentLatitude=0.0;
	double currentLongitude=0.0; 
	// JSON node keys for providers
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
	private String destinationAddress = null;
	private String startingAddress = null;
	private String uriString = "http://maps.google.com/maps?saddr=start_lat,start_lon&daddr=end_lat,end_lot";
	private String uriString2 = "http://maps.google.com/maps?saddr={start_address}&daddr={destination_address}";
	private String uri;
	



	// JSON node keys for Complaints
	private static final String TAG_COMPLAINTS = "providercomplaints";


	private static final String TAG_COMPLAINTTITLE = "complaint_title";
	private static final String TAG_COMPLAINTRESOLVED = "resolved";

	JSONArray complaints = null;
	ArrayList<HashMap<String, String>> complaintList = new ArrayList<HashMap<String, String>>();

	// HTTP Stuff
	private static final String complaintSearchURL = "http://54.201.44.59:3000/providercomplaints.json?utf8=%E2%9C%93&search=";

	private static String complaintFullSearchURL = null;


	GoogleMap mMap;


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_result, menu);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_list_item);
		mLocationClient = new LocationClient(this, this, this);
		mCurrentLocation = mLocationClient.getLastLocation();
		Button getDirections = (Button) findViewById(R.id.maps_button);
		currentLatitude = mCurrentLocation.getLatitude();
		currentLongitude = mCurrentLocation.getLongitude();

		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		String providerName = in.getStringExtra(TAG_PROVIDERNAME);
		String licenseInfo = in.getStringExtra(TAG_LICENSEINFO);
		String ownerName = in.getStringExtra(TAG_OWNERNAME);
		String address = in.getStringExtra(TAG_ADDRESS);
		String city = in.getStringExtra(TAG_CITY);
		String state = in.getStringExtra(TAG_STATE);
		String zipCode = in.getStringExtra(TAG_ZIPCODE);
		String phoneNumber = in.getStringExtra(TAG_PHONENUMBER);
		String latitude = in.getStringExtra(TAG_LATITUDE);
		String longitude = in.getStringExtra(TAG_LONGITUDE);
		String capacity = in.getStringExtra(TAG_CAPACITY);
		String hours = in.getStringExtra(TAG_HOURS);
		String specialist = in.getStringExtra(TAG_SPECIALIST);
		String specialistPhone = in.getStringExtra(TAG_SPECIALISTPHONE);
		String qualityLevel = in.getStringExtra(TAG_QUALITY);

		// Displaying all values on the screen
		TextView lblName = (TextView) findViewById(R.id.name_label);
		TextView lblLicense = (TextView) findViewById(R.id.license_label);
		TextView lblOwner = (TextView) findViewById(R.id.owner_label);
		TextView lblAddress = (TextView) findViewById(R.id.address_label);
		TextView lblCity = (TextView) findViewById(R.id.city_label);
		// TextView lblState = (TextView) findViewById(R.id.state_label);
		// TextView lblZipcode = (TextView) findViewById(R.id.zipcode_label);
		TextView lblPhone = (TextView) findViewById(R.id.phone_label);
		TextView lblCapacity = (TextView) findViewById(R.id.capacity_label);
		TextView lblhours = (TextView) findViewById(R.id.hours_label);
		TextView lblSpecialist = (TextView) findViewById(R.id.specialist_label);
		TextView lblSpecialistPhone = (TextView) findViewById(R.id.specialistPhone_label);
		TextView lblQuality = (TextView) findViewById(R.id.qualityLevel_label);

		/** DISPLAY PROVIDER INFO **/

		lblName.setText(providerName);
		lblLicense.setText(licenseInfo);
		lblOwner.setText(ownerName);
		lblAddress.setText(address);
		lblCity.setText(city + ", " + state + " " + zipCode);
		// lblState.setText(state);
		// lblZipcode.setText(zipCode);
		lblPhone.setText(phoneNumber);
		Linkify.addLinks(lblPhone, Linkify.PHONE_NUMBERS); //set phonenumber to clickable
		lblCapacity.setText(capacity);
		lblhours.setText(hours);
		lblSpecialist.setText(specialist);
		lblSpecialistPhone.setText(specialistPhone);
		lblQuality.setText(qualityLevel);

		/** DISPLAY MAP **/
		double dbl_latitude = Double.parseDouble(latitude);
		double dbl_longitude = Double.parseDouble(longitude);

		LatLng PROVIDER_LOCATION = new LatLng(dbl_latitude, dbl_longitude);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		mMap.addMarker(new MarkerOptions().position(
				new LatLng(dbl_latitude, dbl_longitude)).title(providerName));

		mMap.moveCamera(CameraUpdateFactory
				.newLatLngZoom(PROVIDER_LOCATION, 14));

		/** DISPLAY COMPLAINT DATA **/

		complaintFullSearchURL = complaintSearchURL + providerName;

		JSONParser jComplaintParser = new JSONParser();
		String complaintActualSearch = complaintFullSearchURL.replace(" ", "+");
		JSONObject complaintjson = jComplaintParser
				.getJSONFromUrl(complaintActualSearch);

		System.out.println("COMPLAINT HTTP SUCCESSFUL");
		try {
			// get the array of providers
			System.out.println("CREATING THE COMPLAINTS JSON ARRAY");

			complaints = complaintjson.getJSONArray(TAG_COMPLAINTS);
			System.out.println("Beginning For Loop to go through array");


			for (int i = 0; i < complaints.length(); i++) {
				JSONObject permit = complaints.getJSONObject(i);

				// store the json items in variables

				String complaintName = permit.getString(TAG_COMPLAINTTITLE);
				String complaintResolved = permit
						.getString(TAG_COMPLAINTRESOLVED);

				HashMap<String, String> cmap = new HashMap<String, String>();

				cmap.put(TAG_COMPLAINTTITLE, complaintName);
				cmap.put(TAG_COMPLAINTRESOLVED, complaintResolved);

				// add Hashlist to ArrayList
				System.out
				.println("Adding Tags to Map, adding map to providerList");
				complaintList.add(cmap);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// //Display parsed Complaint data-DO THIS********************
		//
		
		destinationAddress = address;
		//startingAddress = ;
		uri = "http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr={"+destinationAddress+"}";
		
		getDirections.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
				startActivity(intent);
			}
		});

		TextView complaintsTableLabel = (TextView) findViewById(R.id.complaints_Table_label);
		complaintsTableLabel.setText("Complaints: ");

		TableLayout complaintTable = (TableLayout) findViewById(R.id.complaintTable);
		complaintTable.setStretchAllColumns(true);

		System.out.println("Building Table");

		for (int j = 0; j < complaintList.size(); j++) {
			TableRow complaintRow = new TableRow(SingleMenuItemActivity.this);
			System.out.println("Building Table");

			String complaintNameData = complaintList.get(j).get(
					TAG_COMPLAINTTITLE);
			String complaintResolvedData = complaintList.get(j).get(
					TAG_COMPLAINTRESOLVED);

			Log.d("What ComplaintData says",
					complaintList.get(j).get(TAG_COMPLAINTTITLE));
			Log.d("What ComplaintData says",
					complaintList.get(j).get(TAG_COMPLAINTRESOLVED));

			TextView lblComplaintName = new TextView(this);
			TextView lblComplaintResolved = new TextView(this);

			lblComplaintName.setText(complaintNameData);
			lblComplaintName.setPadding(20, 20, 20, 20);
			lblComplaintResolved.setText(complaintResolvedData);
			lblComplaintResolved.setPadding(20, 20, 20, 20);

			complaintRow.addView(lblComplaintName);

			complaintRow.addView(lblComplaintResolved);

			complaintTable.addView(complaintRow);

		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						this,
						9000);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace(); }
		} else {
			/*
			 * If no resolution is available, display a dialog to the
			 * user with the error.
			 */
			System.out.println(connectionResult.getErrorCode());
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}
}

