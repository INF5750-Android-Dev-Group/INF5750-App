package no.uio.inf5750.assignment3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;

public class ConnectionManager {
	private static ConnectionManager mManager = null;
	private String mUsername, mPassword, mLog;

	static {
		mManager =  new ConnectionManager();
	}

	private ConnectionManager() 
	{
		mUsername = "admin";
		mPassword = "district";
		mLog = "";
	}

	public static ConnectionManager getConnectionManager() 
	{
		return mManager;
	}

	public String getUsername() 
	{
		return mUsername;
	}

	public void setUsername(String username) 
	{
		mUsername = username;
	}

	public void setPassword(String password) 
	{
		mPassword = password;
	}

	public Drawable getImage(String url)
	{
		Drawable drawable = null;

		HttpClient httpclient = new DefaultHttpClient();

		String data = mUsername+':'+mPassword;

		addToLog("Starting image request..");

		String encoding = new String(Base64.encodeBase64(data.getBytes()));
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Basic " + encoding);

		addToLog("executing image request " + httpget.getRequestLine());
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpEntity entity = null;
		if(response!=null)
		{
			entity = response.getEntity();
		}

		if(entity!=null)
		{
			try {
				InputStream is = entity.getContent();
				drawable = Drawable.createFromStream(is, "src name");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("entity is null!");
		}

		return drawable;
	}

	public String doRequest(String url)
	{
		String output = "";

		HttpClient httpclient = new DefaultHttpClient();

		String data = mUsername+':'+mPassword;

		String encoding = new String(Base64.encodeBase64(data.getBytes()));
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Basic " + encoding);

		HttpResponse response = null;
		addToLog("Starting request..");
		boolean failed = false;
		addToLog(url);
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			failed = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			failed = true;
		}
		if (failed) {
			addToLog("Request failed.");
			return "";
		}
		addToLog("Done.");
		HttpEntity entity = null;
		if(response!=null)
		{
			entity = response.getEntity();
		}

		if(entity!=null)
		{
			try {


				InputStream is = entity.getContent();
				Scanner sc = new Scanner(is);
				output = "";
				while (sc.hasNext()) {
					output += sc.nextLine();
				}

			} catch (IllegalStateException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return output;

	}

	private void addToLog(String lines) 
	{
		mLog += lines + "\n";
	}
	public String getLog() 
	{
		return mLog;
	}

	public void clearLog() 
	{
		mLog = "";
	}

}
