package no.uio.inf5750.assignment3.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * ConnectionManager is used to communicate with the server and store
 * authentication information
 * @author Jan Anders Bremer
 */

public class ConnectionManager {
	private static ConnectionManager mManager = null;
	private String mUsername, mPassword, mLog, mSite;
	private ConnectivityManager mConn = null;
	private Context mContext = null;

	/**
	 * Makes this class a singleton
	 */
	static {
		mManager =  new ConnectionManager();
	}

	/**
	 * Only a private constructor is available for this class
	 */
	private ConnectionManager() 
	{
		mUsername = "admin";
		mPassword = "district";
		mLog = "";
		mSite = "http://apps.dhis2.org/dev/api/";
	}
	
	/**
	 * Changes the site the app uses
	 * @param site The new site
	 */
	public void setSite(String site) {
		mSite = site;
	}
	
	/**
	 * @return The current site URL.
	 */
	public String getSite() {
		return mSite;
	}

	/**
	 * @return The ConnectionManager instance
	 */
	public static ConnectionManager getConnectionManager() 
	{
		return mManager;
	}
	
	public void setContext(Context c) {
		mContext = c;
	}
	
	/**
	 * @return Online status
	 */
	public boolean getOnlineStatus() {
		if (mConn == null) {
			mConn = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo info = mConn.getActiveNetworkInfo();
		if (info == null) {
		    return false;
		}
		return info.isConnected();
	}
	/**
	 * @return Username of the current user
	 */
	public String getUsername() 
	{
		return mUsername;
	}

	/**
	 * Changes username
	 * @param username The new user
	 */
	public void setUsername(String username) 
	{
		mUsername = username;
	}

	/**
	 * Changes password
	 * @param password New password
	 */
	public void setPassword(String password) 
	{
		mPassword = password;
	}
	
	/**
	 * Used to reply to a message
	 * @param id Position in message-array
	 * @param message The reply
	 */
	public void replyMessage(int id, String message) {
		doPost(UpdateDaemon.getDaemon().getMessage(id).getConversationUrl(), message);
	}
	
	/**
	 * Used to reply to a message
	 * @param msgId String containing the messageId
	 * @param message The reply 
	 */
	public void replyMessage(String msgId, String message) {
		doPost(Message.getConversationUrl(msgId), message);
	}
	
	/**
	 * Adds a comment to an interpretation
	 * @param interId String containg the interpretation Id
	 * @param message The reply
	 */
	public void replyInterpretation(String interId, String message) {
		doPost(Interpretation.getInterpretationUrl(interId), message);
	}

	/**
	 * @return Base64 encoded username and password.
	 */
	private String getEncodedUserInfo() {
		return new String(Base64.encodeBase64((mUsername+':'+mPassword).getBytes()));
	}
	
	/**
	 * Retrieves a given image
	 * @param url The location of the image
	 * @return Drawable containing the image
	 */
	public Drawable getImage(String url)
	{
		if (!getOnlineStatus()) {
			return null;
		}
		Drawable drawable = null;

		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Basic " + getEncodedUserInfo());

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
	
	/**
	 * Executes a HTTP post with a given content
	 * @param url POST URL
	 * @param content The content to be sent in the post
	 */
	boolean doPost(String url, String content) {
		if (!getOnlineStatus()) {
			return false;
		}
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Authorization", "Basic " + getEncodedUserInfo());
		try {
			httppost.setEntity(new StringEntity(content));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		HttpClient httpclient = new DefaultHttpClient();
		
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Retrieves a given url 
	 * @param url The url retrieved
	 * @return The content of what the url is pointing at
	 */
	public String doRequest(String url)
	{
		if (!getOnlineStatus()) {
			return "";
		}
		String output = "";

		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Basic " + getEncodedUserInfo());

		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
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

}
