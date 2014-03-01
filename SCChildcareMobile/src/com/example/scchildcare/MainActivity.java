/**
 * 
 * Version 0.0.9
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.example.scchildcare;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public final static String EXTRA_LONGITUDE = null;
	public final static String EXTRA_LATITUDE = null;

	
	EditText editText;
	// Label instructing input for EditText
	TextView geocodeLabel;
	// EditText textbox1; //new hidden textbox
	ImageButton button1;
	// Text box for entering address
	EditText addressText;
	// android:onClick="getLocation"android:onClick="getLocation"private
	// TextView LongLat;
	private LocationClient mLocationClient;
	Location mCurrentLocation;
	//private TextView LongLat1;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private MainFragment mainFragment;

	public static final String APPTAG = "Location Updates";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */

				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(), APPTAG);
			}
			return false;
		}

	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// geocodeLabel = (TextView) findViewById(R.id.geocodeLabel);
		// geocodeLabel.setText(getString(R.string.geocode_label));
		// LongLat = (TextView) findViewById(R.id.lat_lng);
		
		mLocationClient = new LocationClient(this, this, this);
		// LongLat1 = (TextView) findViewById(R.id.lat_lng1);
		button1 = (ImageButton) findViewById(R.id.add_comment_button);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}

		// // gets the activity's default ActionBar
		// ActionBar actionBar = getActionBar();
		// actionBar.show();
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		// addressText.getText().toString();
		// addressText = (EditText) findViewById(R.id.addressText);
		System.out.println("I've resumed");
		super.onResume();
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

	private void showErrorDialog(int errorCode) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(), APPTAG);
		}

	}

	public void getLocation(View v) {

		// If Google Play Services is available
		if (servicesConnected()) {

			// Get the current location
			mCurrentLocation = mLocationClient.getLastLocation();

			// Display the current location in the UI
			double latitude = mCurrentLocation.getLatitude();
			double longitude = mCurrentLocation.getLongitude();
			String longlat = "latitude " + Double.toString(latitude) + " "
					+ "longitude " + Double.toString(longitude);
			System.out.println(longlat);
			// LongLat1.setText(longlat);

			String param_latitude = Double.toString(latitude);
			String param_longitude = Double.toString(longitude);

			System.out.println(param_latitude + ", " + param_longitude);

			makeToast("Searching, Please wait...");
			Intent gpsSearch = new Intent(this, GPS_SearchResultsActivity.class);
			System.out.println("GPS_SearchResultsActivity created");
			Bundle coordinates = new Bundle();
			coordinates.putString("EXTRA_LATITUDE", param_latitude);
			coordinates.putString("EXTRA_LONGITUDE", param_longitude);

			System.out.println(coordinates);
			gpsSearch.putExtras(coordinates);
			startActivity(gpsSearch);

		}
	}

	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view) {
		makeToast("Searching, Please wait...");
		Intent intent = new Intent(this, SearchResultsActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString().replace(" ", "+");

		//String message = mainFragment.locate2(view);
		intent.putExtra(EXTRA_MESSAGE, message);
		/**
		 * If there is no Connection to the server, this will error out.
		 * 
		 * Possibly a try/catch on the getJSONFromURL method in
		 * SearchResultsActivity?
		 */

		startActivity(intent);

	}

	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
