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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.uio.inf5750.assignment3.diagram.DiagramActivity;
import no.uio.inf5750.assignment3.map.MapActivity;
import no.uio.inf5750.assignment3.messaging.MessagingActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button mButtonDiagram;
	private Button mButtonMap;
	private Button mButtonMessaging;
	
	private Context mContext;
	
	private String mUsername, mPassword;
	
	private TextView mTextView;
	private String mOutput;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Look in res/layout/ for the layout definitions.
        mUsername = "admin";
        mPassword = "district";
        
        mContext = this;
        
        setButtons();
        
        mOutput = "";
        mTextView = (TextView) findViewById(R.id.main_infotext);
        
        String response = doSampleRequest("http://apps.dhis2.org/dev/api/interpretations/cjG99uolq7c.xml");
        print(response);
        
        Document domElement = getDomElement(response);
        
        //getValue()..
        //getElementValue()..

    }
    
    public String doSampleRequest(String url)
    {
    	String output = "";
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	
    	String data = mUsername+':'+mPassword;
    	
    	print("Starting request..");
    	
    	String encoding = new String(Base64.encodeBase64(data.getBytes()));
    	HttpGet httppost = new HttpGet(url);
    	httppost.setHeader("Authorization", "Basic " + encoding);
    	
    	print("executing request " + httppost.getRequestLine());
    	HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
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
				byte[] bytes = new byte[2048];
				
				InputStream is = entity.getContent();
				is.read(bytes);
				
				output= new String(bytes);
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return output;
          
    }
    
    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
                // return DOM
            return doc;
    }
    
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
     
    public final String getElementValue( Node elem ) {
             Node child;
             if( elem != null){
                 if (elem.hasChildNodes()){
                     for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                         if( child.getNodeType() == Node.TEXT_NODE  ){
                             return child.getNodeValue();
                         }
                     }
                 }
             }
             return "";
      } 
    
    public void print(String output)
    {
    	mOutput += output + "\n\n";
    	mTextView.setText(mOutput);
    }
    
    public void setButtons()
    {
        mButtonDiagram = (Button) findViewById(R.id.main_button_diagram);
        mButtonMap = (Button) findViewById(R.id.main_button_map);
        mButtonMessaging = (Button) findViewById(R.id.main_button_messaging);
    	
    	mButtonDiagram.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, DiagramActivity.class);
				startActivity(intent);
				
			}
        	
        });
        
        mButtonMap.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, MapActivity.class);
				startActivity(intent);
				
			}
        	
        });
        
        mButtonMessaging.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, MessagingActivity.class);
				startActivity(intent);
				
			}
        	
        });
    }
}