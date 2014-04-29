package com.example.scchildcare.test;

import com.example.scchildcare.ConnectionErrorActivity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class ConnectionErrorActivityTest extends ActivityUnitTestCase<ConnectionErrorActivity>{
	private ConnectionErrorActivity mCEA;
	public ConnectionErrorActivityTest() {
		super(ConnectionErrorActivity.class);
	}
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(), ConnectionErrorActivity.class);
		startActivity(intent, null, null);
		mCEA = getActivity();
	}
}
