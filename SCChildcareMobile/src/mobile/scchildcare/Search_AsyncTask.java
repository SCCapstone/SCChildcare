package mobile.scchildcare;

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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * AsyncTask us a collection of methods designed to speed up processing and
 * networking of the app. These run in the background of the activities.
 * 
 * @author Ben
 * 
 */
public class Search_AsyncTask extends FragmentActivity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	String message = "";
	ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.p_bar);
		// //////////////////////////////////////////

		// //////////////////////////////////////////
		Intent anIntent = getIntent();
		message = anIntent.getStringExtra(EXTRA_MESSAGE);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		GetSearchResults searchResults = new GetSearchResults(
				getApplicationContext(), this, progressBar);
		searchResults.execute(message);

	}

	/**
	 * This subclass builds the URL to search the server, goes to the server,
	 * then collects the data and sends it to the correct Activity.
	 * 
	 * @author Ben
	 * 
	 */
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
		Search_AsyncTask search;
		boolean isConnected = false;
		ProgressBar pBar;

		private GetSearchResults(Context context, Search_AsyncTask b,
				ProgressBar theBar) {
			aContext = context;
			search = b;
			pBar = theBar;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

			if (isConnected == true) {
				Intent SearchResults = new Intent(getApplicationContext(),
						SearchResultsActivity.class);
				SearchResults.putExtra(TAG_LIST_OF_PROVIDERS,
						(Serializable) result);
				pBar.setVisibility(View.INVISIBLE);
				search.finish();
				startActivity(SearchResults);
			} else {
				Intent noConnect = new Intent(aContext.getApplicationContext(),
						ConnectionErrorActivity.class);
				pBar.setVisibility(View.INVISIBLE);
				search.finish();
				startActivity(noConnect);
			}

		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {

			aMessage = params[0];

			System.out.println(" A messsga " + aMessage);
			fullSearchURL = searchURL + aMessage;
			if (isURLReachable(aContext)) {
				isConnected = true;
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
			} else {
				isConnected = false;
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

}
