package no.uio.inf5750.assignment3;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

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
	

	public String doRequest(String url)
	{
		String output = "";

		HttpClient httpclient = new DefaultHttpClient();

		String data = mUsername+':'+mPassword;

		String encoding = new String(Base64.encodeBase64(data.getBytes()));
		HttpGet httppost = new HttpGet(url);
		httppost.setHeader("Authorization", "Basic " + encoding);

		HttpResponse response = null;
		addToLog("Starting request..");
		boolean failed = false;
		addToLog(url);
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
				byte[] bytes = new byte[2048];

				InputStream is = entity.getContent();
				is.read(bytes);

				output= new String(bytes);

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
