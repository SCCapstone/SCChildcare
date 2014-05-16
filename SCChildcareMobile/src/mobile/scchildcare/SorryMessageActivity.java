package mobile.scchildcare;

import mobile.scchildcare.R;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SorryMessageActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sorry_message, menu);
		return true;
	}

	@SuppressLint("NewApi")
	/**
	 * When the app cannot read the data, or gets no search results, the user will see this page
	 * 
	 * Note:
	 * 		This page needs to be fleshed out a bit more, as it is currently very vague. 
	 * 
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sorry);

		// Get the message from the intent
		// Intent intent = getIntent();
		String sorry = getString(R.string.sorry);

		// Create the text view
		TextView textView = (TextView) findViewById(R.id.sorry_label);
		// textView.setTextSize(25);
		textView.setTextSize(40);
		textView.setText(sorry);

		// Set the text view as the activity layout

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}