package com.example.scchildcare.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.scchildcare.*;

import junit.framework.TestCase;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity mockMainActivity;
	private EditText mockaddressText; 
	private EditText mockEditText;
	private Button mockButton1;
	public MainActivityTest()
	{
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		mockMainActivity = getActivity();
		mockButton1 = (Button) mockMainActivity.findViewById(R.id.
	}
	public void testPreconditios() {
		assertNotNull("mockMainActivity is null", mockMainActivity);
		assertNotNull("mockEditText is null", mockEditText);
		assertNotNull("mockaddressText is null", mockaddressText);
		assertNotNull("mockButton1", mockButton1);
	}
}
