package com.example.scchildcare.test;

import com.example.scchildcare.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private EditText mEditText;
	
	
	public MainActivityTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
		mEditText = (EditText) mainActivity.findViewById(com.example.scchildcare.R.id.edit_message);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
