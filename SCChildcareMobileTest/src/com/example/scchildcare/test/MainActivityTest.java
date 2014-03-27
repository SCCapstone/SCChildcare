package com.example.scchildcare.test;

import com.example.scchildcare.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

   private MainActivity mainActivity;
   private EditText mEditText;
   private ImageButton b1;
   private ImageButton b2;
	
	
	public MainActivityTest(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
		mEditText = (EditText) mainActivity.findViewById(com.example.scchildcare.R.id.edit_message);
		b1 = (ImageButton) mainActivity.findViewById(com.example.scchildcare.R.id.button1);
		b2 = (ImageButton) mainActivity.findViewById(com.example.scchildcare.R.id.button2);
	}
	
	 public void testPreconditions() {
		    assertNotNull("Main Activity is null", mainActivity);
	        assertNotNull("editText is null", mEditText);
	        assertNotNull("button 1 is null", b1);
	        assertNotNull("button 2 is null", b2);
	    }
	
	public void testGPSlocationButton(){
	//make sure that clicking the button issues intent	
		
	}
	
	public void testSendMessageButton(){
		//make sure that clicking the button issues intent	
			
			
		}
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
