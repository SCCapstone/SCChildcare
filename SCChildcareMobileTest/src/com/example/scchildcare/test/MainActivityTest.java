package com.example.scchildcare.test;

import com.example.scchildcare.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
  //  private EditText mFirstTestText;
	
	
	public MainActivityTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
