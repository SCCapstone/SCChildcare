/**
 * 
 */
package com.example.scchildcare.test;
import com.example.scchildcare.*;


import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

/**
 * @author Confederate803
 *
 */
public class SorryMessageTest extends ActivityInstrumentationTestCase2<SorryMessageActivity> {
	private SorryMessageActivity smessageactivity; 
	private TextView textview; 
	/**
	 * @param name
	 */
	public SorryMessageTest(String name) {
		super(SorryMessageActivity.class);
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		smessageactivity = getActivity();
		textview = (TextView) smessageactivity.findViewById(com.example.scchildcare.R.id.sorry_label);
	}
	public void testPreconditions() {
		assertNotNull("smessageactivity is null", smessageactivity);
		assertNotNull("textview is null", textview); 
	}

}
