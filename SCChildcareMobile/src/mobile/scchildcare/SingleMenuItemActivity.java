package mobile.scchildcare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SingleMenuItemActivity extends Activity {

	// JSON node keys for providers
	private static final String TAG_PROVIDERID = "id";
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
	String pID;
	String theMarker = "";
	private static final String TAG_GET_COMMENTS = "grabbing comments";

	// JSON node keys for Complaints
	private static final String TAG_COMPLAINTS = "providercomplaints";
	private static final String TAG_COMPLAINTTYPE = "complaint_type";
	private static final String TAG_COMPLAINTDATE = "issueDate";
	private static final String TAG_COMPLAINTRESOLVED = "resolved";
	private static final String TAG_CENTER_DATA = "dataforcenter";

	// JSON nodes for Comments
	private static final String TAG_ALIAS = "user";
	private static final String TAG_BODY = "body";
	private static final String TAG_PROVIDER_COMMENT_ID = "provider_id";
	private static final String TAG_JSON_COMMENT = "comment";
	String provider_comment_ID;
	HttpResponse response;
	HashMap<String, String> map = new HashMap<String, String>();
	String lat1;
	String long1;
	double lat_1;
	double long_1;

	private static final String commentAddURL1 = "http://54.201.44.59:3000/providers/";
	private static final String commentAddURL2 = "/comments";

	HashMap<String, String> commentMap = new HashMap<String, String>();
	ArrayList<HashMap<String, String>> commentList = new ArrayList<HashMap<String, String>>();

	JSONParser jParser = new JSONParser();
	JSONArray comments = null;

	GoogleMap mMap;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_result, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	/**
	 * This class builds the single provider page for the app. 
	 * 
	 * It collects the data given from the search results list click, and gets the current user's location for 
	 * navigation purposes. After building the basic design, the map is built, with a closer look at the centers
	 * location. 
	 * 
	 * The class then dynamically builds the complaint tables and the comment section
	 *
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_list_item);

		// getting intent data
		Intent in = getIntent();
		lat1 = in.getStringExtra("YOURLAT");
		long1 = in.getStringExtra("YOURLONG");

		Bundle getProviderData = in.getExtras();
		ArrayList<HashMap<String, String>> complaintList = new ArrayList<HashMap<String, String>>();
		complaintList = (ArrayList<HashMap<String, String>>) getProviderData
				.getSerializable(TAG_COMPLAINTS);
		map = (HashMap<String, String>) getProviderData
				.getSerializable(TAG_CENTER_DATA);

		// ///////////////////////////////////////////////////////////
		commentList = (ArrayList<HashMap<String, String>>) getProviderData
				.getSerializable(TAG_GET_COMMENTS);
		// //////////////////////////////////////////////////////////

		// ////////////////////////////////////////////
		// Get JSON values from previous intent
		final String providerID = map.get(TAG_PROVIDERID);
		String providerName = map.get(TAG_PROVIDERNAME);
		pID = providerID;
		String licenseInfo = map.get(TAG_LICENSEINFO);
		String ownerName = map.get(TAG_OWNERNAME);
		String address = map.get(TAG_ADDRESS);
		String city = map.get(TAG_CITY);
		String state = map.get(TAG_STATE);
		String zipCode = map.get(TAG_ZIPCODE);
		String phoneNumber = map.get(TAG_PHONENUMBER);
		String latitude = map.get(TAG_LATITUDE);
		String longitude = map.get(TAG_LONGITUDE);
		String capacity = map.get(TAG_CAPACITY);
		String hours = map.get(TAG_HOURS);
		String specialist = map.get(TAG_SPECIALIST);
		String specialistPhone = map.get(TAG_SPECIALISTPHONE);
		String qualityLevel = map.get(TAG_QUALITY).replace("null",
				"Not Participating");

		provider_comment_ID = providerID;

		// Log.d("PROVIDER ID: ", providerID);

		// ///////////////////////////////////////////////////////////////////////////////////////////////
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
		Linkify.addLinks(lblPhone, Linkify.PHONE_NUMBERS);
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
		final String destinationAddress = address;
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker aMarker) {
				theMarker = (aMarker.getTitle());
				aMarker.showInfoWindow();
				if (!long1.equals("") && !lat1.equals("")) {
					lat_1 = Double.parseDouble(lat1);
					long_1 = Double.parseDouble(long1);

					String uri = "http://maps.google.com/maps?saddr=" + lat_1
							+ "," + long_1 + "&daddr={" + destinationAddress
							+ "}";
					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(uri));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);
				} else {
					GPS_check();
				}
				return true;
			}

		});

		/** DISPLAY COMPLAINT DATA **/

		TextView complaintsTableLabel = (TextView) findViewById(R.id.complaints_Table_label);
		complaintsTableLabel.setText("Complaints: ");

		TableLayout complaintTable = (TableLayout) findViewById(R.id.complaintTable);
		complaintTable.setStretchAllColumns(true);

		System.out.println("Building Table");

		for (int j = 0; j < complaintList.size(); j++) {
			TableRow complaintRow = new TableRow(SingleMenuItemActivity.this);
			System.out.println("Building Table");

			String complaintTypeData = complaintList.get(j).get(
					TAG_COMPLAINTTYPE);
			String complaintIssueDate = complaintList.get(j).get(
					TAG_COMPLAINTDATE);
			String complaintResolvedData = complaintList.get(j).get(
					TAG_COMPLAINTRESOLVED);

			TextView lblComplaintType = new TextView(this);
			TextView lblComplaintDate = new TextView(this);
			TextView lblComplaintResolved = new TextView(this);

			lblComplaintType.setText(complaintTypeData);
			lblComplaintType.setTextSize(15);
			lblComplaintType.setPadding(10, 20, 100, 20);
			lblComplaintDate.setText(complaintIssueDate);
			lblComplaintDate.setTextSize(15);
			lblComplaintDate.setGravity(Gravity.CENTER);
			lblComplaintDate.setPadding(0, 20, 0, 20);
			lblComplaintResolved.setText(complaintResolvedData);
			lblComplaintResolved.setTextSize(15);
			lblComplaintResolved.setGravity(Gravity.CENTER);
			lblComplaintResolved.setPadding(50, 20, 20, 20);

			complaintRow.setGravity(Gravity.CENTER);
			complaintRow.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			complaintRow.addView(lblComplaintType);

			complaintRow.addView(lblComplaintDate);

			complaintRow.addView(lblComplaintResolved);

			complaintTable.addView(complaintRow);

		}
		// /////////////////////////////////

		// System.out.println("BREAK TEST");
		TableLayout commentTable = (TableLayout) findViewById(R.id.commentTable);
		// System.out.println("BREAK TEST");
		complaintTable.setStretchAllColumns(true);
		//
		// System.out.println("Building Comment Table");

		for (int k = 0; k < commentList.size(); k++) {
			TableRow aliasRow = new TableRow(SingleMenuItemActivity.this);
			TableRow commentRow = new TableRow(SingleMenuItemActivity.this);
			System.out.println("Building Table");

			String commentAlias = commentList.get(k).get(TAG_ALIAS);
			String commentBody = commentList.get(k).get(TAG_BODY);

			TextView lblCommentAlias = new TextView(this);
			TextView lblCommentBody = new TextView(this);

			lblCommentAlias.setText(commentAlias);
			lblCommentAlias.setTypeface(null, Typeface.ITALIC);
			lblCommentAlias.setTextColor(Color.parseColor("#0069a3"));
			lblCommentAlias.setTextSize(17);
			lblCommentAlias.setPadding(10, 10, 10, 10);
			lblCommentBody.setText(commentBody);
			lblCommentBody.setTextSize(17);
			lblCommentBody.setPadding(10, 10, 10, 60);

			aliasRow.addView(lblCommentAlias);

			commentRow.addView(lblCommentBody);

			commentTable.addView(aliasRow);
			commentTable.addView(commentRow);

		}

		// //////////////////////////////////
		// Comment System Stuff
		/***********************************************************************
  *        
  */

		final EditText aliasText = (EditText) findViewById(R.id.aliasText);
		final EditText commentText = (EditText) findViewById(R.id.commentText);

		aliasText.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View a, MotionEvent event) {

				a.setFocusable(true);
				a.setFocusableInTouchMode(true);
				return false;
			}
		});

		commentText.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View c, MotionEvent event) {

				c.setFocusable(true);
				c.setFocusableInTouchMode(true);
				return false;
			}
		});

		Button addComment = (Button) findViewById(R.id.add_comment_button);

		/**
		 * 
		 * When the user adds a comment, the alias and body are checked to
		 * ensure they are in the correct value boundaries. Then the app reloads
		 * the page with the new comments when clicked.
		 */
		addComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EditText aliasText = (EditText) findViewById(R.id.aliasText);
				// aliasText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				EditText commentText = (EditText) findViewById(R.id.commentText);

				String alias = aliasText.getText().toString();
				String comment = commentText.getText().toString();

				// //////////////////////////////////////////////

				if (alias.length() < 5) {
					AliasAlert();
				} else if (comment.length() < 10) {
					CommentAlert();
				} else {
					commentSystem cSystem = new commentSystem(
							SingleMenuItemActivity.this, map);
					cSystem.execute(alias, comment, pID);
				}

			}

		});
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	class commentSystem extends AsyncTask<String, String, String> {
		Activity theActivity;
		JSONObject commentJSON = new JSONObject();
		JSONObject JSON = new JSONObject();
		HttpClient postClient = new DefaultHttpClient();
		String proID;
		// HttpConnectionParams.setConnectionTimeout(postClient.getParams(),
		// 10000);
		String postURL = commentAddURL1 + provider_comment_ID + commentAddURL2;
		HashMap<String, String> theMap = new HashMap<String, String>();

		commentSystem(Activity anActivity, HashMap<String, String> aMap) {
			theActivity = anActivity;
			theMap = aMap;
		}

		@Override
		protected void onPostExecute(String here) {
			theActivity.finish();
			Intent anIntent = new Intent(getApplicationContext(),
					Single_AsyncTask.class);
			anIntent.putExtra(TAG_CENTER_DATA, (Serializable) map);
			anIntent.putExtra("THE_PROVIDER", proID);
			startActivity(anIntent);

		}

		protected String doInBackground(String... args) {

			String alias = args[0];
			String comment = args[1];
			proID = args[2];
			System.out.println(alias + " " + comment);
			try {
				HttpPost post = new HttpPost(postURL);

				commentJSON.put(TAG_PROVIDER_COMMENT_ID, provider_comment_ID);
				commentJSON.put(TAG_ALIAS, alias);
				commentJSON.put(TAG_BODY, comment);
				JSON.put(TAG_JSON_COMMENT, commentJSON);

				StringEntity se = new StringEntity(JSON.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setHeader("Content-Type", "application/json");
				// se.toString();
				// System.out.println(se);
				post.setEntity(se);
				response = postClient.execute(post);

			} catch (Exception e) {
				e.printStackTrace();
				// createDialog("Error", "Cannot Estabilish Connection");
			}

			return null;
		}

	}

	private void AliasAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage("Name must contain a minimum of 5 characters")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private void CommentAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage("Comment must contain a minimum of 10 characters")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();

	}

	private void GPS_check() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage("Must use the GPS search to use this feature")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
}

