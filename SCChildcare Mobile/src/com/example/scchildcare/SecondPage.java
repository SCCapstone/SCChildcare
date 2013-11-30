package com.example.scchildcare;

import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class SecondPage extends Activity{
	Button button;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_page);
	}
}