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

import android.accounts.AccountManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
public final static String EXTRA_LONGITUDE = null;
public final static String EXTRA_LATITUDE = null;
private long mLastClickTime = 0;
boolean check;
//JSONParser jParser = new JSONParser();
////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
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
//private TextView LongLat1;
private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
LocationManager locationManager;
public static final String APPTAG = "Location Updates";
boolean done = false;

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
//Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

}

void getResult(Boolean abool){
	check = abool;
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

 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 System.out.println("button1");
///////////////////////////////////////////
 button1 = (ImageButton) findViewById(R.id.button1);
 System.out.println("button2");
 button2 = (ImageButton) findViewById(R.id.button_2);
 System.out.println("button1onclick");
 
 button1.setOnClickListener(new View.OnClickListener(){

	@Override
	public void onClick(View v)
	{
		
		 if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
	            return;
	        }
	        mLastClickTime = SystemClock.elapsedRealtime();	
///////////////////////	
		
		
			// If Google Play Services is available
			if (servicesConnected() && isNetworkAvailable()) {
				
				if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				

			// Get the current location
			mCurrentLocation = mLocationClient.getLastLocation();

			// Display the current location in the UI
			double latitude = mCurrentLocation.getLatitude();
			double longitude = mCurrentLocation.getLongitude();
			String longlat = "latitude " + Double.toString(latitude) + " "
			+ "longitude " + Double.toString(longitude);
			System.out.println(longlat);

			String param_latitude = Double.toString(latitude);
			String param_longitude = Double.toString(longitude);

			//////////////////////////////////////////
			Intent GPSSearch = new Intent(getApplicationContext(), GPS_Search_AsyncTask.class);
			Bundle coordinates = new Bundle();
			 coordinates.putString("EXTRA_LATITUDE", param_latitude);
			 coordinates.putString("EXTRA_LONGITUDE", param_longitude);
			 GPSSearch.putExtras(coordinates);
			 startActivity(GPSSearch);
			
			}
			else{
			//makeToast("Internet connection not established");
			 if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) != true){
				    showGPSDisabledAlertToUser();
				}		
			}
			}
			else{
				 showConnectionAlertToUser();
			}
		
	}
	 
 });
 System.out.println("button2onclick");
 button2.setOnClickListener(new View.OnClickListener(){

	@Override
	public void onClick(View v) 
	{
	
		
		//////////////////////////	
		 if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
	            return;
	        }
	        mLastClickTime = SystemClock.elapsedRealtime();	
///////////////////////	
		
		
		if(isNetworkAvailable()){	
		//	Intent intent = new Intent(this, SearchResultsActivity.class);
			editText = (EditText)findViewById(R.id.edit_message);
			String message2 = editText.getText().toString();
            String message = editText.getText().toString().replace(" ", "%20");
            if(!message2.isEmpty())
            {
			//intent.putExtra(EXTRA_MESSAGE, message);
		//	startActivity(intent);
            Intent intent = new Intent(getApplicationContext(), Search_AsyncTask.class);
            intent.putExtra(EXTRA_MESSAGE, message);
    		startActivity(intent);
           
            }
            else
            {
            MessageToUser();	     	
            }
			}
			else{
				
		   showConnectionAlertToUser();	
							
			}
	
	}
 });
 
}
////////////////////////////////////////
private void showConnectionAlertToUser(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("This app requires an internet connection. Would you like to enable it?")
    .setCancelable(false)
    .setPositiveButton("Go to Settings Page To Enable Internet Connection",
            new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int id){
            Intent wirelessIntent = new Intent(
                    android.provider.Settings.ACTION_WIFI_SETTINGS);
            startActivity(wirelessIntent);
        }
    });
    alertDialogBuilder.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int id){
            dialog.cancel();
        }
    });
    AlertDialog alert = alertDialogBuilder.create();
    alert.show();
}
///////////////////////
private void MessageToUser(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Must enter a child care provider name, address, or city")
    .setCancelable(false)
    .setPositiveButton("OK",
            new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int id){
        	dialog.cancel();
        }
    });
    AlertDialog alert = alertDialogBuilder.create();
    alert.show();
}
///////////////////////////////////////////////////////////
@Override
public void onDisconnected() {
// Display the connection status
//Toast.makeText(this, "Disconnected. Please re-connect.",
//Toast.LENGTH_SHORT).show();
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
protected void onResume()
{
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

private boolean isNetworkAvailable()
{
ConnectivityManager connectivityManager
= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isConnectedOrConnecting();
}

private void showGPSDisabledAlertToUser(){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
    .setCancelable(false)
    .setPositiveButton("Go to Settings Page To Enable GPS",
            new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int id){
            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(callGPSSettingIntent);
        }
    });
    alertDialogBuilder.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int id){
            dialog.cancel();
        }
    });
    AlertDialog alert = alertDialogBuilder.create();
    alert.show();
}


}


