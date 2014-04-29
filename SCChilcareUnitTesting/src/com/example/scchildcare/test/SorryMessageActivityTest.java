package com.example.scchildcare.test;

import com.example.scchildcare.SorryMessageActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class SorryMessageActivityTest extends ActivityUnitTestCase<SorryMessageActivity>{
	SorryMessageActivity sma;
	public SorryMessageActivityTest() {
		super(SorryMessageActivity.class);
	}
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getContext(),SorryMessageActivity.class);
		startActivity(intent,null,null);
		sma = getActivity();
	}
}
