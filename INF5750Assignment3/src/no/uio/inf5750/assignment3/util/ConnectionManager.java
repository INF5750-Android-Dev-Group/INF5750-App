package no.uio.inf5750.assignment3.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.drawable.Drawable;

public class ConnectionManager {
	private static ConnectionManager mManager = null;
	private String mUsername, mPassword, mLog, mSite;

	static {
		mManager =  new ConnectionManager();
	}

	private ConnectionManager() 
	{
		mUsername = "admin";
		mPassword = "district";
		mLog = "";
		mSite = "http://apps.dhis2.org/dev/api/";
	}
	
	public void setSite(String site) {
		mSite = site;
	}
	
	public String getSite() {
		return mSite;
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
	
	public void replyMessage(int id, String message) {
		doPost(UpdateDaemon.getDaemon().getMessage(id).getConversationUrl(), message);
	}
	
	public void replyMessage(String msgId, String message) {
		// TODO
	}
	
	public void sendNewMessage(String personId, String title, String message) {
		// TODO
	}
	
	public void replyInterpretation(String interId, String message) {
		// TODO
	}

	private String getEncodedUserInfo() {
		return new String(Base64.encodeBase64((mUsername+':'+mPassword).getBytes()));
	}
	
	public Drawable getImage(String url)
	{
		Drawable drawable = null;

		HttpClient httpclient = new DefaultHttpClient();


		addToLog("Starting image request..");

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Basic " + getEncodedUserInfo());

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
	
	public void doPost(String url, String content) {
		
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Authorization", "Basic " + getEncodedUserInfo());
		List<NameValuePair> post = new ArrayList<NameValuePair>(1);
		post.add(new BasicNameValuePair("text", content));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(post));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		HttpClient httpclient = new DefaultHttpClient();
		boolean failed = false;
		
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			failed = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			failed = true;
		}
		
	}

	public String doRequest(String url)
	{
		String output = "";

		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Basic " + getEncodedUserInfo());

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
