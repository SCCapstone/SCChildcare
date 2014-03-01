
package com.example.scchildcare;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SingleMenuItemActivity extends Activity {

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

	// JSON node keys for Permits
	private static final String TAG_PERMITS = "permits";
	private static final String TAG_PERMITNAME = "permitName";
	private static final String TAG_PERMITEXPIRATION = "permitExpiration";
	JSONArray permits = null;
	ArrayList<HashMap<String, String>> permitList = new ArrayList<HashMap<String, String>>();

	// JSON node keys for Complaints
	private static final String TAG_COMPLAINTS = "complaints";
	private static final String TAG_COMPLAINTRESOLVED = "complaintResolved";
	private static final String TAG_COMPLAINTNAME = "complaintName";
	JSONArray complaints = null;
	ArrayList<HashMap<String, String>> complaintList = new ArrayList<HashMap<String, String>>();

	// HTTP Stuff
	private static final String permitSearchURL = "http://54.201.44.59:3000/providerpermits.json?utf8=%E2%9C%93&search=";
	private static final String complaintSearchURL = "http://54.201.44.59:3000/providercomplaints.json?utf8=%E2%9C%93&search=";
	private static String permitFullSearchURL = null;
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

		/** DISPLAY PERMIT DATA **/

		// Parse Data
		permitFullSearchURL = permitSearchURL + providerName;

		JSONParser jPermitParser = new JSONParser();
		String permitActualSearch = permitFullSearchURL.replace(" ", "+");
		Log.d("TESTING FOR PROPER SEARCH", permitActualSearch);

		JSONObject permitjson = jPermitParser
				.getJSONFromUrl(permitActualSearch);

		System.out.println("PERMIT HTTP SUCCESSFUL");
		try {
			// get the array of providers
			System.out.println("CREATING THE PERMITS JSON ARRAY");

			permits = permitjson.getJSONArray(TAG_PERMITS);

			System.out.println("Beginning For Loop to go through array");

			if (permits.length() == 0) {
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
				for (int i = 0; i < permits.length(); i++) {
					JSONObject permit = permits.getJSONObject(i);

					// store the json items in variables

					String permitName = permit.getString(TAG_PERMITNAME);
					String permitExpiration = permit
							.getString(TAG_PERMITEXPIRATION);

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(TAG_PERMITNAME, permitName);
					map.put(TAG_PERMITEXPIRATION, permitExpiration);

					// add Hashlist to ArrayList
					System.out
							.println("Adding Tags to Map, adding map to providerList");
					permitList.add(map);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

//		// Display parsed Permit data-DO THIS********************
//
//		TextView permitsTableLabel = (TextView) findViewById(R.id.permits_Table_label);
//		permitsTableLabel.setText("Permits: ");
//		
//		TableLayout permitTable = (TableLayout) findViewById(R.id.permitTable);
//		
//		System.out.println("Building Table");
//
//
//		TableRow permitRow;
//		for (int i = 0; i < permitList.size(); i++) {
//			System.out.println("Building Table");
//			LayoutInflater inflater = LayoutInflater
//					.from(SingleMenuItemActivity.this);
//			permitRow = (TableRow) inflater.inflate(R.id.permitTableRow,
//					permitTable, false);
//
//			String permitNameData = permitList.get(i).get(TAG_PERMITNAME);
//			String permitExpirationData = permitList.get(i).get(
//					TAG_PERMITEXPIRATION);
//			
//			Log.d("What PermitData says", permitList.get(i).get(TAG_PERMITNAME));
//			Log.d("What PermitData says", permitList.get(i).get(
//					TAG_PERMITEXPIRATION));
//
//			TextView lblPermitName = (TextView) findViewById(R.id.permit_name_label);
//			TextView lblPermitExpiration = (TextView) findViewById(R.id.permit_expiration_label);
//
//			lblPermitName.setText(permitNameData);
//			lblPermitExpiration.setText(permitExpirationData);
//
//			permitTable.addView(permitRow);
//
//		}
		
		

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

			if (complaints.length() == 0) {
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
				for (int i = 0; i < complaints.length(); i++) {
					JSONObject permit = complaints.getJSONObject(i);

					// store the json items in variables

					String complaintName = permit.getString(TAG_COMPLAINTNAME);
					String complaintResolved = permit
							.getString(TAG_COMPLAINTRESOLVED);

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(TAG_COMPLAINTNAME, complaintName);
					map.put(TAG_COMPLAINTRESOLVED, complaintResolved);

					// add Hashlist to ArrayList
					System.out
							.println("Adding Tags to Map, adding map to providerList");
					permitList.add(map);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// //Display parsed Complaint data-DO THIS********************
		//
		// ListAdapter complaintAdapter = new SimpleAdapter(this, complaintList,
		// R.layout.list_item, new String[] { TAG_PERMITNAME,
		// TAG_PERMITEXPIRATION }, new int[] { R.id.name, R.id.licenseInfo,
		// R.id.ownerName, R.id.address, R.id.city, R.id.state,
		// R.id.zipCode, R.id.phone, R.id.latitude,
		// R.id.longitude, R.id.capacity, R.id.hours, R.id.specialist,
		// R.id.specialistPhone,
		// R.id.qualityLevel });
		//

		// ******setListAdapter(complaintAdapter);

	}

}

