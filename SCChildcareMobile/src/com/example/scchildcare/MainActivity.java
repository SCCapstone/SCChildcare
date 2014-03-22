/**
 * 
 * Version 0.0.7
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.example.scchildcare;


import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.plus.sample.quickstart.R;



public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>, 
		View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public final static String EXTRA_LONGITUDE = null;
	public final static String EXTRA_LATITUDE = null;

	private GoogleApiClient mGoogleApiClient;
	private static final int RC_SIGN_IN = 0;
	private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;
	private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
	private static final String SAVED_PROGRESS = "sign_in_progress";
	
	 /* A flag indicating that a PendingIntent is in progress and prevents
	   * us from starting further intents.
	   */
	  private boolean mIntentInProgress;
	  
	  private static final String TAG = "android-plus-quickstart";
	  
	  private SignInButton mSignInButton;
	  private Button mSignOutButton;
	  private Button mRevokeButton;
	  private TextView mStatus;
	  private ListView mCirclesListView;
	  private ArrayAdapter<String> mCirclesAdapter;
	  private ArrayList<String> mCirclesList;
	  
	  private int mSignInProgress;
	  
	  // Used to store the PendingIntent most recently returned by Google Play
	  // services until the user clicks 'sign in'.
	  private PendingIntent mSignInIntent;
	  
	  // Used to store the error code most recently returned by Google Play services
	  // until the user clicks 'sign in'.
	  private int mSignInError;



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
			 switch (requestCode) {
		      case RC_SIGN_IN:
		        if (resultCode == RESULT_OK) {
		          // If the error resolution was successful we should continue
		          // processing errors.
		          mSignInProgress = STATE_SIGN_IN;
		        } else {
		          // If the error resolution was not successful or the user canceled,
		          // we should stop processing errors.
		          mSignInProgress = STATE_DEFAULT;
		        }
		        
		        if (!mGoogleApiClient.isConnecting()) {
		          // If Google Play services resolved the issue with a dialog then
		          // onStart is not called so we need to re-attempt connection here.
		          mGoogleApiClient.connect();
		        }
		        break;
		    }
			

		}
		super.onActivityResult(requestCode, resultCode, data);
		
		 if (requestCode == RC_SIGN_IN) {
			    mIntentInProgress = false;

			    if (!mGoogleApiClient.isConnecting()) {
			      mGoogleApiClient.connect();
			    }
			  }
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
		
		  // Reaching onConnected means we consider the user signed in.
	    Log.i(TAG, "onConnected");
	    
	    // Update the user interface to reflect that the user is signed in.
	    mSignInButton.setEnabled(false);
	    mSignOutButton.setEnabled(true);
	    mRevokeButton.setEnabled(true);
	    
	    // Retrieve some profile information to personalize our app for the user.
	    Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
	    
	    mStatus.setText(String.format(
	        getResources().getString(R.string.signed_in_as),
	        currentUser.getDisplayName()));

	    Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
	        .setResultCallback(this);
	    
	    // Indicate that the sign in process is complete.
	    mSignInProgress = STATE_DEFAULT;

	}

	private GoogleApiClient buildGoogleApiClient() {
	    // When we build the GoogleApiClient we specify where connected and
	    // connection failed callbacks should be returned, which Google APIs our
	    // app uses and which OAuth 2.0 scopes our app requests.
	    return new GoogleApiClient.Builder(this)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .addApi(Plus.API, null)
	        .addScope(Plus.SCOPE_PLUS_LOGIN)
	        .build();
	  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocationClient = new LocationClient(this, this, this);
		button1 = (ImageButton) findViewById(R.id.button1);
		
		//editText = (EditText)findViewById(R.id.edit_message);
		
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
		
		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
	    mSignOutButton = (Button) findViewById(R.id.sign_out_button);
	    mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
	    mStatus = (TextView) findViewById(R.id.sign_in_status);
	    mCirclesListView = (ListView) findViewById(R.id.circles_list);
	    
	    mSignInButton.setOnClickListener(this);
	    mSignOutButton.setOnClickListener(this);
	    mRevokeButton.setOnClickListener(this);
	    
	    mCirclesList = new ArrayList<String>();
	    mCirclesAdapter = new ArrayAdapter<String>(
	        this, R.layout.circle_member, mCirclesList);
	    mCirclesListView.setAdapter(mCirclesAdapter);
	    
	    if (savedInstanceState != null) {
	      mSignInProgress = savedInstanceState
	          .getInt(SAVED_PROGRESS, STATE_DEFAULT);
	    }
	    
	    mGoogleApiClient = buildGoogleApiClient();
		
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
		
		   Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
			        + result.getErrorCode());
			    
			    if (mSignInProgress != STATE_IN_PROGRESS) {
			      // We do not have an intent in progress so we should store the latest
			      // error resolution intent for use when the sign in button is clicked.
			      mSignInIntent = result.getResolution();
			      mSignInError = result.getErrorCode();
			      
			      if (mSignInProgress == STATE_SIGN_IN) {
			        // STATE_SIGN_IN indicates the user already clicked the sign in button
			        // so we should continue processing errors until the user is signed in
			        // or they click cancel.
			        resolveSignInError();
			      }
			    }
			    
			    // In this sample we consider the user signed out whenever they do not have
			    // a connection to Google Play services.
			    onSignedOut();

	}
	
	  private void resolveSignInError() {
		    if (mSignInIntent != null) {
		      // We have an intent which will allow our user to sign in or
		      // resolve an error.  For example if the user needs to
		      // select an account to sign in with, or if they need to consent
		      // to the permissions your app is requesting.

		      try {
		        // Send the pending intent that we stored on the most recent
		        // OnConnectionFailed callback.  This will allow the user to
		        // resolve the error currently preventing our connection to
		        // Google Play services.  
		        mSignInProgress = STATE_IN_PROGRESS;
		        startIntentSenderForResult(mSignInIntent.getIntentSender(),
		            RC_SIGN_IN, null, 0, 0, 0);
		      } catch (SendIntentException e) {
		        Log.i(TAG, "Sign in intent could not be sent: "
		            + e.getLocalizedMessage());
		        // The intent was canceled before it was sent.  Attempt to connect to
		        // get an updated ConnectionResult.
		        mSignInProgress = STATE_SIGN_IN;
		        mGoogleApiClient.connect();
		      }
		    } else {
		      // Google Play services wasn't able to provide an intent for some
		      // error types, so we show the default Google Play services error
		      // dialog which may still start an intent on our behalf if the
		      // user can resolve the issue.
		      showDialog(DIALOG_PLAY_SERVICES_ERROR);
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
		mGoogleApiClient.connect();

	}

	@Override
	protected void onStop() {
		
		super.onStop();
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		
		 if (mGoogleApiClient.isConnected()) {
		      mGoogleApiClient.disconnect();
		    }
		
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
		if (servicesConnected() && isNetworkAvailable()) {

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
		else{
			makeToast("Internet connection not established");
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

		@Override
		  protected Dialog onCreateDialog(int id) {
		    switch(id) {
		      case DIALOG_PLAY_SERVICES_ERROR:
		        if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
		          return GooglePlayServicesUtil.getErrorDialog(
		              mSignInError,
		              this,
		              RC_SIGN_IN, 
		              new DialogInterface.OnCancelListener() {
		                @Override
		                public void onCancel(DialogInterface dialog) {
		                  Log.e(TAG, "Google Play services resolution cancelled");
		                  mSignInProgress = STATE_DEFAULT;
		                  mStatus.setText(R.string.status_signed_out);
		                }
		              });
		        } else {
		          return new AlertDialog.Builder(this)
		              .setMessage(R.string.play_services_error)
		              .setPositiveButton(R.string.close,
		                  new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                      Log.e(TAG, "Google Play services error could not be "
		                          + "resolved: " + mSignInError);
		                      mSignInProgress = STATE_DEFAULT;
		                      mStatus.setText(R.string.status_signed_out);
		                    }
		                  }).create();
		        }
		      default:
		        return super.onCreateDialog(id);
		    }
		  }		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendMessage(View view)
	{
		if(isNetworkAvailable()){
		makeToast("Searching, Please {wait...");	
		Intent intent = new Intent(this, SearchResultsActivity.class);
	    editText = (EditText)findViewById(R.id.edit_message);
		
        String message = editText.getText().toString().replace(" ", "%20");

		intent.putExtra(EXTRA_MESSAGE, message);
		/**
		 * If there is no Connection to the server, this will error out.
		 * 
		 * Possibly a try/catch on the getJSONFromURL method in
		 * SearchResultsActivity?
		 */
		startActivity(intent);
	}
		else{
			makeToast("Internet connection not established");	
		}
			
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
	
	public void onConnectionSuspended(int cause) {
		  mGoogleApiClient.connect();
		}
	
	 @Override
	  protected void onSaveInstanceState(Bundle outState) 
	 {
	    super.onSaveInstanceState(outState);
	    outState.putInt(SAVED_PROGRESS, mSignInProgress);
	  }

	  @Override
	  public void onResult(LoadPeopleResult peopleData) {
	    if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
	      mCirclesList.clear();
	      PersonBuffer personBuffer = peopleData.getPersonBuffer();
	      try {
	          int count = personBuffer.getCount();
	          for (int i = 0; i < count; i++) {
	              mCirclesList.add(personBuffer.get(i).getDisplayName());
	          }
	      } finally {
	          personBuffer.close();
	      }

	      mCirclesAdapter.notifyDataSetChanged();
	    } else {
	      Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
	    }
	  }
	  
	  private void onSignedOut() {
		    // Update the UI to reflect that the user is signed out.
		    mSignInButton.setEnabled(true);
		    mSignOutButton.setEnabled(false);
		    mRevokeButton.setEnabled(false);
		    
		    mStatus.setText(R.string.status_signed_out);
		    
		    mCirclesList.clear();
		    mCirclesAdapter.notifyDataSetChanged();
		  }

	  @Override
	  public void onClick(View v) {
	    if (!mGoogleApiClient.isConnecting()) {
	      // We only process button clicks when GoogleApiClient is not transitioning
	      // between connected and not connected.
	      switch (v.getId()) {
	          case R.id.sign_in_button:
	            mStatus.setText(R.string.status_signing_in);
	            resolveSignInError();
	            break;
	          case R.id.sign_out_button:
	            // We clear the default account on sign out so that Google Play
	            // services will not return an onConnected callback without user
	            // interaction.
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            mGoogleApiClient.disconnect();
	            mGoogleApiClient.connect();
	            break;
	          case R.id.revoke_access_button:
	            // After we revoke permissions for the user with a GoogleApiClient
	            // instance, we must discard it and create a new one.
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            // Our sample has caches no user data from Google+, however we
	            // would normally register a callback on revokeAccessAndDisconnect
	            // to delete user data so that we comply with Google developer
	            // policies.
	            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
	            mGoogleApiClient = buildGoogleApiClient();
	            mGoogleApiClient.connect();
	            break;
	      }
	    }
	  }
	  
	  
	  

	
	
}
