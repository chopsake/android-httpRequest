package com.countableset.hellohttppost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HelloHTTPPost extends Activity
{
	// View Variable
	private ListView lv1;

	// List array of friends
	private String[] friends;

	private static final int HTTP_STATUS_OK = 200;
	private static final String URL = "http://dev.countableset.com/android/friends.php";
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";

	// Menu Items
	private static final int REFRESH_ID = Menu.FIRST;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get the post data to fill the friends array
		postData();

		// List Activity Settings
		lv1 = (ListView) findViewById(R.id.ListView01);
		lv1.setAdapter(new ArrayAdapter<String>(this, R.layout.list_view,
				friends));
	} // end onCreat()

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		// When you press the menu button
		menu.add(0, REFRESH_ID, 0, R.string.refresh);
		return true;
	} // end Menu Button

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
		case REFRESH_ID:
			postData();
			lv1.setAdapter(new ArrayAdapter<String>(this, R.layout.list_view,
					friends));
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	} // end Menu Selection

	public void postData()
	{
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL);

		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			// Check for a good connect and whether is has been established
			if (response.getStatusLine().getStatusCode() == HTTP_STATUS_OK)
			{
				HttpEntity entity = response.getEntity();

				if (entity != null)
				{
					InputStream instream = entity.getContent();

					// Load the page converted to a string into a JSONObject
					JSONObject myAwway = new JSONObject(
							convertStreamToString(instream));

					// Create the array of names
					JSONArray names = myAwway.getJSONArray("names");

					// Initialize the array to the correct length of friends
					friends = new String[names.length()];

					// Fill the friends array with the JSONObject
					for (int i = 0; i < names.length(); i++)
						friends[i] = names.getString(i);

					instream.close();

				} // end if
			} // end if
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
		} catch (JSONException ex)
		{

		}
	}

	private static String convertStreamToString(InputStream is)
	{
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				is.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}