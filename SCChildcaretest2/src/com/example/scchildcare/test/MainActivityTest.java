package com.example.scchildcare.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.scchildcare.*;

import junit.framework.TestCase;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity mockMainActivity;
	private EditText mockEditText; 
	public MainActivityTest()
	{
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mockMainActivity = getActivity();
	}
	public void testPreconditios() {
		assertNotNull("mockMainActivity is null", mockMainActivity);
		assertNotNull("mockEditText is null",mockEditText);
	}
}
