

package de.pajowu.donate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
/**
 * 
 * @author livin
 *
 */
public class ServerConnector {
	Context context;

	public ServerConnector(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}


	
	/**
	 * Returns a JSONObject by calling the url
	 * @param url
	 * @return JSONObject
	 */
	public static JSONObject getJSONObjectfromURL(String url) {
		Log.e("TO SERVER ",""+url);
		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jObject = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
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
			
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObject = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jObject;
	}
	
}
