/**
 * 
 * Version 0.1.4
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

package com.example.scchildcare;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
	private long mLastClickTime = 0;
	// JSONParser jParser = new JSONParser();
	// //////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////
	EditText editText;
	// Label instructing input for EditText
	TextView geocodeLabel;
	// EditText textbox1; //new hidden textbox
	ImageButton button1;
	ImageButton button2;
	// Text box for entering address
	EditText addressText;
	// android:onClick="getLocation"android:onClick="getLocation"private
	// TextView LongLat;
	private LocationClient mLocationClient;
	Location mCurrentLocation;
	// private TextView LongLat1;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	//private MainFragment mainFragment;
	LocationManager locationManager;
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
		// Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		//
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("Open instance");
		super.onCreate(savedInstanceState);
		System.out.println("set layout");
		setContentView(R.layout.activity_main);
		System.out.println("new locationclient");
		mLocationClient = new LocationClient(this, this, this);
		System.out.println("getsystemservice");

		// editText = (EditText)findViewById(R.id.edit_message);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		System.out.println("button1");
		// /////////////////////////////////////////
		button1 = (ImageButton) findViewById(R.id.button1);
		System.out.println("button2");
		button2 = (ImageButton) findViewById(R.id.button_2);
		System.out.println("button1onclick");

		// editText.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// v.setFocusable(true);
		// v.setFocusableInTouchMode(true);
		// return false;
		// }
		// });

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				// /////////////////////

				// If Google Play Services is available
				if (servicesConnected() && isNetworkAvailable()) {

					if (locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

						// Get the current location
						mCurrentLocation = mLocationClient.getLastLocation();

						// Display the current location in the UI
						double latitude = mCurrentLocation.getLatitude();
						double longitude = mCurrentLocation.getLongitude();
						String longlat = "latitude "
								+ Double.toString(latitude) + " "
								+ "longitude " + Double.toString(longitude);
						System.out.println(longlat);

						String param_latitude = Double.toString(latitude);
						String param_longitude = Double.toString(longitude);
						// //////////////////////////////////////////
						// String param_latitude = Double.toString(33.984209);
						// String param_longitude = Double.toString(-81.075269);

						// /////////////////////////////////////////

						// ////////////////////////////////////////
						GetGPSResults gpsResults = new GetGPSResults(v
								.getContext());
						gpsResults.execute(param_latitude, param_longitude);

					} else {
						// makeToast("Internet connection not established");
						if (locationManager
								.isProviderEnabled(LocationManager.GPS_PROVIDER) != true) {
							showGPSDisabledAlertToUser();
						}
					}
				} else {
					showConnectionAlertToUser();
				}

			}

		});
		System.out.println("button2onclick");
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// ////////////////////////
				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				// /////////////////////

				if (isNetworkAvailable()) {
					// Intent intent = new Intent(this,
					// SearchResultsActivity.class);
					editText = (EditText) findViewById(R.id.edit_message);
					String message2 = editText.getText().toString();
					String message = editText.getText().toString()
							.replace(" ", "%20");
					if (!message2.isEmpty()) {
						// intent.putExtra(EXTRA_MESSAGE, message);
						// startActivity(intent);
						GetSearchResults searchResults = new GetSearchResults(v
								.getContext());
						searchResults.execute(message);
					} else {
						MessageToUser();
					}
				} else {

					showConnectionAlertToUser();

				}

			}
		});

	}

	// //////////////////////////////////////
	private void showConnectionAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"This app requires an internet connection. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton(
						"Go to Settings Page To Enable Internet Connection",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent wirelessIntent = new Intent(
										android.provider.Settings.ACTION_WIFI_SETTINGS);
								startActivity(wirelessIntent);
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

	// /////////////////////
	private void MessageToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"Must enter a child care provider name, address, or city")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	// /////////////////////////////////////////////////////////
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

	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected()
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Go to Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
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

	// //////////////////////////////////////////////////////////////////////////
	class GetGPSResults extends
			AsyncTask<String, String, ArrayList<HashMap<String, String>>> {
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
		// private ProgressBar progressBar;
		ArrayList<HashMap<String, String>> storeData = new ArrayList<HashMap<String, String>>();
		// JSON node names

		// ProgressBar pBar;

		Context aContext;

		private GetGPSResults(Context context) {
			aContext = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pBar.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			Intent GPSSearch = new Intent(aContext.getApplicationContext(),
					GPS_SearchResultsActivity.class);
			GPSSearch.putExtra(TAG_LIST_OF_PROVIDERS, (Serializable) result);
			Bundle coordinates = new Bundle();
			coordinates.putString("EXTRA_LATITUDE", latit);
			coordinates.putString("EXTRA_LONGITUDE", longit);
			GPSSearch.putExtras(coordinates);
			// pBar.setVisibility(View.INVISIBLE);
			startActivity(GPSSearch);
		}

		protected ArrayList<HashMap<String, String>> doInBackground(
				String... args) {

			String param_latitude = args[0];
			String param_longitude = args[1];
			latit = args[0];
			longit = args[1];
			System.out.println(param_longitude + " " + param_latitude
					+ " in do background");
			fullGPS_URL = gpsURL_1 + param_longitude + gpsURL_2
					+ param_latitude;

			// System.out.println("Beginning JSON Parse");
			if (isURLReachable(aContext) == true) {
				System.out.println(" server is running ");

				if (!param_longitude.isEmpty() && !param_latitude.isEmpty()) {
					// ThreadPolicy tp = ThreadPolicy.LAX;
					// StrictMode.setThreadPolicy(tp);
					// System.out.println("Getting JSON with HTTP");
					JSONObject json = jParser.getJSONFromUrl(fullGPS_URL);
					if (json.length() > 0) {
						System.out.println("non null json object");
					}
					try {
						providers = json.getJSONArray(TAG_PROVIDERS);
						// System.out.println(json.toString());
						System.out.println(" providers length "
								+ providers.length());
						if (providers.length() > 0) {
							System.out.println(" providers list is not empty ");
							for (int i = 0; i < providers.length(); i++) {
								JSONObject p = providers.getJSONObject(i);
								String id = p.getString(TAG_ID);
								String providerName = p
										.getString(TAG_PROVIDERNAME);
								String licenseInfo = p
										.getString(TAG_LICENSEINFO);
								String ownerName = p.getString(TAG_OWNERNAME);
								String address = p.getString(TAG_ADDRESS);
								String city = p.getString(TAG_CITY);
								String state = p.getString(TAG_STATE);
								String zipCode = p.getString(TAG_ZIPCODE);
								String phoneNumber = p
										.getString(TAG_PHONENUMBER);
								String longitude = p.getString(TAG_LONGITUDE);
								String latitude = p.getString(TAG_LATITUDE);
								String capacity = p.getString(TAG_CAPACITY);
								String hours = p.getString(TAG_HOURS);
								String specialist = p.getString(TAG_SPECIALIST);
								String specialistPhone = p
										.getString(TAG_SPECIALISTPHONE);
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
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return storeData;
		}

		public boolean isURLReachable(Context context) {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL("http://54.201.44.59"); // Change to
																// "http://google.com"
																// for www test.
					HttpURLConnection urlc = (HttpURLConnection) url
							.openConnection();
					urlc.setConnectTimeout(10 * 1000); // 10 s.
					urlc.connect();
					if (urlc.getResponseCode() == 200) { // 200 = "OK" code
															// (http connection
															// is fine).
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

	class GetSearchResults extends
			AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

		String searchURL = "http://54.201.44.59:3000/providers.json?utf8=%E2%9C%93&search=";
		// private static byte[] buff = new byte[1024];
		// private static String result = null;
		String fullSearchURL = null;
		String aMessage = "";
		// JSON node names
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
		// String message;
		ArrayList<HashMap<String, String>> storeData = new ArrayList<HashMap<String, String>>();
		JSONParser jParser = new JSONParser();
		JSONArray providers = null;
		String TAG_LIST_OF_PROVIDERS = "pList";
		Context aContext;

		private GetSearchResults(Context context) {
			aContext = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			Intent SearchResults = new Intent(getApplicationContext(),
					SearchResultsActivity.class);
			SearchResults
					.putExtra(TAG_LIST_OF_PROVIDERS, (Serializable) result);
			startActivity(SearchResults);

		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {

			aMessage = params[0];

			System.out.println(" A messsga " + aMessage);
			fullSearchURL = searchURL + aMessage;
			if (isURLReachable(aContext)) {
				if (!aMessage.isEmpty()) {
					System.out.println(aMessage);
					JSONObject json = jParser.getJSONFromUrl(fullSearchURL);
					// System.out.println(json.toString());
					try {
						// get the array of providers
						System.out.println("CREATING THE PROVIDERS JSON ARRAY");

						providers = json.getJSONArray(TAG_PROVIDERS);

						System.out
								.println("Beginning For Loop to go through array");
						System.out.println(" provider.length "
								+ providers.length());

						if (providers.length() > 0) {
							System.out.println("No Return on Search");

							for (int i = 0; i < providers.length(); i++) {
								JSONObject p = providers.getJSONObject(i);

								// store the json items in variables
								String id = p.getString(TAG_ID);
								String providerName = p
										.getString(TAG_PROVIDERNAME);
								String licenseInfo = p
										.getString(TAG_LICENSEINFO);
								String ownerName = p.getString(TAG_OWNERNAME);
								String address = p.getString(TAG_ADDRESS);
								String city = p.getString(TAG_CITY);
								String state = p.getString(TAG_STATE);
								String zipCode = p.getString(TAG_ZIPCODE);
								String phoneNumber = p
										.getString(TAG_PHONENUMBER);
								String longitude = p.getString(TAG_LONGITUDE);
								String latitude = p.getString(TAG_LATITUDE);
								String capacity = p.getString(TAG_CAPACITY);
								String hours = p.getString(TAG_HOURS);
								String specialist = p.getString(TAG_SPECIALIST);
								String specialistPhone = p
										.getString(TAG_SPECIALISTPHONE);
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
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
			return storeData;
		}

		public boolean isURLReachable(Context context) {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL("http://54.201.44.59"); // Change to
																// "http://google.com"
																// for www test.
					HttpURLConnection urlc = (HttpURLConnection) url
							.openConnection();
					urlc.setConnectTimeout(10 * 1000); // 10 s.
					urlc.connect();
					if (urlc.getResponseCode() == 200) { // 200 = "OK" code
															// (http connection
															// is fine).
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
	// ///////////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////
}
