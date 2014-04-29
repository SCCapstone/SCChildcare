package com.example.scchildcare.test;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.example.scchildcare.MainActivity;
import com.example.scchildcare.GPS_Search_AsyncTask;
import com.example.scchildcare.Search_AsyncTask;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private MainActivity mActivity;
	private int buttonnum1 = com.example.scchildcare.R.id.button1;
	private int buttonnum2 = com.example.scchildcare.R.id.button_2;
	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
	}
	
	
	public void layoutTest1() throws Exception
	{
		ActivityMonitor m1 = getInstrumentation().addMonitor(GPS_Search_AsyncTask.class.getName(),null,false);
		//assertNotNull(mActivity.findViewById(buttonnum1));
		Button view1= (Button) mActivity.findViewById(buttonnum1);
		TouchUtils.clickView(this, view1);
		GPS_Search_AsyncTask gps = (GPS_Search_AsyncTask) m1.waitForActivityWithTimeout(4000);
		assertNotNull("GPS_Search_AsyncTask is null!!", gps);
		//assertEquals("Failed Assertion","Button1",view1.getText());
		//assertNotNull(mActivity.findViewById(buttonnum2));
		//assertEquals("Failed Assertion","Button2",view2.getText());
	}	
	public void layoutTest2() throws Exception 
	{
		ActivityMonitor m2 = getInstrumentation().addMonitor(Search_AsyncTask.class.getName(),null,false);
		Button view2 = (Button) mActivity.findViewById(buttonnum2);
		TouchUtils.clickView(this,view2);
		Search_AsyncTask search = (Search_AsyncTask) m2.waitForActivityWithTimeout(4000);
		assertNotNull("Search_Async is null!",search);
	}
}
