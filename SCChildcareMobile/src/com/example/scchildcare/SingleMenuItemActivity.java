package com.example.scchildcare;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
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

    // JSON node keys for Complaints
    private static final String TAG_COMPLAINTS = "providercomplaints";
    private static final String TAG_COMPLAINTTYPE = "complaint_type";
    private static final String TAG_COMPLAINTDATE = "issueDate";
    private static final String TAG_COMPLAINTRESOLVED = "resolved";
    private static final String TAG_CENTER_DATA = "dataforcenter";
    
    //JSONArray complaints = null;
   

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

    @SuppressWarnings("unchecked")
@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);

        // getting intent data
        Intent in = getIntent();
        Bundle getProviderData = in.getExtras();
        ArrayList<HashMap<String, String>> complaintList = new ArrayList<HashMap<String, String>>();
        complaintList = (ArrayList<HashMap<String, String>>) getProviderData.getSerializable(TAG_COMPLAINTS);
        HashMap<String, String> map = new HashMap<String, String>();
        map = (HashMap<String, String>)getProviderData.getSerializable(TAG_CENTER_DATA);
               
//////////////////////////////////////////////
        // Get JSON values from previous intent
        String providerName = map.get(TAG_PROVIDERNAME);
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
        String qualityLevel = map.get(TAG_QUALITY);
/////////////////////////////////////////////////////////////////////////////////////////////////
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

        

        /** DISPLAY COMPLAINT DATA **/
       /*
complaintFullSearchURL = complaintSearchURL + providerName;

JSONParser jComplaintParser = new JSONParser();
String complaintActualSearch = complaintFullSearchURL.replace(" ", "+");
JSONObject complaintjson = jComplaintParser
.getJSONFromUrl(complaintActualSearch);
System.out.println("COMPLAINT HTTP SUCCESSFUL");
// get the array of providers
System.out.println("CREATING THE COMPLAINTS JSON ARRAY");

complaints = complaintjson.getJSONArray(TAG_COMPLAINTS);
System.out.println("Beginning For Loop to go through array");
*/
            /*
for (int i = 0; i < complaintList.size(); i++) {
// JSONObject complaint = complaints.getJSONObject(i);

// store the json items in variables

String complaintType = complaint.getString(TAG_COMPLAINTTYPE);
String issueDate = complaint.getString(TAG_COMPLAINTDATE);
String complaintResolved = complaint
.getString(TAG_COMPLAINTRESOLVED);

HashMap<String, String> cmap = new HashMap<String, String>();

cmap.put(TAG_COMPLAINTTYPE, complaintType);
cmap.put(TAG_COMPLAINTDATE, issueDate);
cmap.put(TAG_COMPLAINTRESOLVED, complaintResolved);

// add Hashlist to ArrayList
System.out
.println("Adding Tags to Map, adding map to providerList");
complaintList.add(cmap);

}
*/
       

        // //Display parsed Complaint data-DO THIS********************
        //

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

            Log.d("What ComplaintData says",
                    complaintList.get(j).get(TAG_COMPLAINTTYPE));
            Log.d("What ComplaintData says",
                    complaintList.get(j).get(TAG_COMPLAINTRESOLVED));

            TextView lblComplaintType = new TextView(this);
            TextView lblComplaintDate = new TextView(this);
            TextView lblComplaintResolved = new TextView(this);

            lblComplaintType.setText(complaintTypeData);
            lblComplaintType.setPadding(20, 20, 20, 20);
            lblComplaintDate.setText(complaintIssueDate);
            lblComplaintDate.setPadding(20, 20, 20, 20);
            lblComplaintResolved.setText(complaintResolvedData);
            lblComplaintResolved.setPadding(20, 20, 20, 20);

            complaintRow.addView(lblComplaintType);
            
            complaintRow.addView(lblComplaintDate);

            complaintRow.addView(lblComplaintResolved);

            complaintTable.addView(complaintRow);

        }

        
        
        
    }
    
 

}