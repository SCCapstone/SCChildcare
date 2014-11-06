package mobile.scchildcare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

//import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

public class getJson {

	public static String link = "";
	private InputStream is;
	private StringBuilder sb;
	private String result;


	public static final String postJson(String url,JSONObject Jdata) {
	    // Create a new HttpClient and Post Header

	    HttpParams myParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
	    HttpConnectionParams.setSoTimeout(myParams, 10000);
	    HttpClient httpclient = new DefaultHttpClient(myParams);
	   

	    try {

	        HttpPost httppost = new HttpPost(url.toString());

	        StringEntity se = new StringEntity( Jdata.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
	        String temp = EntityUtils.toString(response.getEntity());
	        Log.i("tag", temp);

	        return temp;

	    } catch (ClientProtocolException e) {

	    } catch (IOException e) {
	    }
	    return "";
	}
	public  static final String postData(String url) {
	    // Create a new HttpClient and Post Header

	    HttpParams myParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
	    HttpConnectionParams.setSoTimeout(myParams, 10000);
	    HttpClient httpclient = new DefaultHttpClient(myParams);
	   

	    try {

	        HttpGet httppost = new HttpGet(url);

	        HttpResponse response = httpclient.execute(httppost);
	        String temp = EntityUtils.toString(response.getEntity());
	        Log.i("tag", temp);

	        return temp;

	    } catch (ClientProtocolException e) {

	    } catch (IOException e) {
	    }
	    return "";
	}
	
	@SuppressLint("NewApi")
	public String getStringGet(String URL) {
		try {
			
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy); 

			HttpClient httpclient = getNewHttpClient();

			HttpGet httppost = new HttpGet(URL);

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.e("log_tag", "result is : " + result.toString());
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());

		}

		return result;
	}
	//@SuppressLint("NewApi")
	public String getString(String URL) {
		try {
			
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy); 

			HttpClient httpclient = getNewHttpClient();

			HttpPost httppost = new HttpPost(URL);

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.e("log_tag", "result is : " + result.toString());
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());

		}

		return result;
	}

	public HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
	
	public boolean haveNetworkConnection(Context context) {
		 ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		    if (connectivity != null) 
		    {
		        NetworkInfo[] info = connectivity.getAllNetworkInfo();

		        if (info != null) 
		        {
		            for (int i = 0; i < info.length; i++) 
		            {
		                Log.i("Class", info[i].getState().toString());
		                if (info[i].getState() == NetworkInfo.State.CONNECTED) 
		                {
		                    return true;
		                }
		            }
		        }
		    }
		    return false;
	}
	
	
}
