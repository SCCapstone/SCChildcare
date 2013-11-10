/**
 * Edited by Ben Gaddy (heavenscloud001) @ 7:00pm 11/3/2013, 9:15am 11/4/2013
 * Edited by Nick Bowe aka Confederate803 at 4:20 pm 11/10/2013
 * Edited by Giovanni Ashman aka ashmang at 7:04 pm 11/3/13
 * Edited by Jilbert Ogunji LeCanadienne at 9:34 pm 11/3/13
 * 
**/
package com.example.scchildcare;

import android.os.Bundle;
import com.facebook.*;
import com.facebook.model.*;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends FragmentActivity {

	private MainFragment mainFragment;
	private static final String TAG = "MainFragment";
	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	}
	
	//not sure what this method does 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
