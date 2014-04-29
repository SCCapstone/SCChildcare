package com.example.scchildcare.test;

import com.example.scchildcare.GPS_JSONParser;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class GPS_JSONParserTest extends ActivityUnitTestCase<GPS_JSONParser>{
	private GPS_JSONParser mGPS_JSONParse;
	public GPS_JSONParserTest() {
		super(GPS_JSONParser.class);
	}
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(), GPS_JSONParser.class);
		startActivity(intent,null,null);
		mGPS_JSONParse=getActivity();
		assertNotNull("GPS_JSONParser is null", mGPS_JSONParse);
	}
}
