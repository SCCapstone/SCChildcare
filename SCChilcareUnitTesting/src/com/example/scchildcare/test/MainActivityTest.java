package com.example.scchildcare.test;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.example.scchildcare.MainActivity;

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
	
	public void layoutTest() throws Exception
	{
		//ActivityMonitor m1 = getInstrumentation().addMonitor(
		//assertNotNull(mActivity.findViewById(buttonnum1));
		Button view1= (Button) mActivity.findViewById(buttonnum1);
		//assertEquals("Failed Assertion","Button1",view1.getText());
		//assertNotNull(mActivity.findViewById(buttonnum2));
		Button view2 = (Button) mActivity.findViewById(buttonnum2);
		//assertEquals("Failed Assertion","Button2",view2.getText());
		TouchUtils.clickView(this, view1);
		
	}	
}
