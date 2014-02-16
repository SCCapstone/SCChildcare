package com.example.scchildcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SingleMenuItemActivity extends Activity {

	// JSON node keys
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

		double dbl_latitude = Double.parseDouble(latitude);
		double dbl_longitude = Double.parseDouble(longitude);
		
		LatLng PROVIDER_LOCATION = new LatLng(dbl_latitude, dbl_longitude);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	
		mMap.addMarker(new MarkerOptions()
		.position(new LatLng(dbl_latitude, dbl_longitude))
				.title(providerName));
		
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PROVIDER_LOCATION, 14));
		
		

	}

}
