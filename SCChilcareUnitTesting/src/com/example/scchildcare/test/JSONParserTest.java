package com.example.scchildcare.test;

import com.example.scchildcare.JSONParser;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class JSONParserTest extends ActivityUnitTestCase<JSONParser> {
	private JSONParser json;
	public JSONParserTest()
	{
		super(JSONParser.class);
	}
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),JSONParser.class);
		startActivity(intent,null,null);
		json = getActivity();
		assertNotNull("JSONParser is null",json);
	}
}
