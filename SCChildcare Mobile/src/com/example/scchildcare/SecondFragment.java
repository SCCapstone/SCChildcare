package com.example.scchildcare;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class SecondFragment extends FragmentActivity {
	// Label instructing input for EditText
			TextView geocodeLabel;
			// Text box for entering address
			EditText addressText;
			private TextView LongLat;
		

		  protected void onCreate(Bundle savedInstanceState)
		  {    
		    
		       setContentView(R.layout.activity_main);	
		    	super.onCreate(savedInstanceState);
				geocodeLabel = (TextView) findViewById(R.id.geocodeLabel);
				geocodeLabel.setText(getString(R.string.geocode_label));
				LongLat = (TextView) findViewById(R.id.lat_lng);
				// Get the addressText component
				addressText = (EditText) findViewById(R.id.addressText);  
		    }
		
		
		  public void locate(View view)
		  {
				// hide virtual keyboard
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(
						getCurrentFocus().getWindowToken(), 0);
				// obtain address from text box
				String address = addressText.getText().toString();
				// set parameters to support the find operation for a geocoding service
				setSearchParams(address);
			}
		  

		private void setSearchParams(String address)
		  {
				// TODO Auto-generated method stub
			  double lat = 0.0;
			  double lng = 0.0;
			  System.out.println(lat + lng);
		try{
			  if (Build.VERSION.SDK_INT >=
		                Build.VERSION_CODES.GINGERBREAD
		                            &&
		                Geocoder.isPresent()) 
			  {
			 Geocoder geoCoder = new Geocoder(this, Locale.US);     
			 List<Address> addresses = geoCoder.getFromLocationName(address , 1);
	         if (addresses.size() > 0) 
	         {            
	         lat = addresses.get(0).getLatitude(); 
	         lng = addresses.get(0).getLongitude();
	         if(addresses.get(0) != null)
	         {
	        	 System.out.println("null fuck I don't give a shit die!!");
	        	 String longlat = "latitude " + Double.toString(lat) + " " + "longitude "
	                  	+  Double.toString(lng);
	                  	System.out.println(longlat +"longlat is ");
	                  	LongLat.setText(longlat);
	         }   
	         }
	     }
		}	  
		catch (Exception e)
		{
			e.printStackTrace();	  
			  
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
}