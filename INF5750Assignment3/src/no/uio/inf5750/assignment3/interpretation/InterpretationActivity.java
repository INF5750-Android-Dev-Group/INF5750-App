package no.uio.inf5750.assignment3.interpretation;

import java.util.ArrayList;

import org.w3c.dom.Document;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import no.uio.inf5750.assignment3.ConnectionManager;
import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import no.uio.inf5750.assignment3.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class InterpretationActivity extends Activity {
	private Spinner mSpnInterpretationList;
	private ViewFlipper mViewFlipper;
	private LinearLayout mLayoutChart, mLayoutInterpretations;
	private ImageView mImageContainer;
	private EditText mEditTextInterpretation;
	private Button mButtonAddInterpretation, mButtonRefresh;
	
	private TextView test;
	
	private float mPrevXtouchValue;
	private int mCurrentInterpretation;
	
	String tester;
	int mNumberOfInterpretations;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interpretations_flipper);
		
		//Initializes variables
		mNumberOfInterpretations = 0;
		mCurrentInterpretation = 0;
		setActivityObjects();
		
		//Updates interpretation list
		update();
		
		
		
		//Fills out the layout with information from first interpretation
		//setData(mCurrentInterpretation)
		
		//Get user information concerning the interpretations the user can access
			//Need an integer value that tells the flipper how many layouts it can generate
				//To be updated when user presses any of the refresh buttons
		
		//Create spinner to navigate between the different interpretations
			//avoids need to load everything to access the last 
		
		//Load the text for all interpretations, when user slides just exchange the content of the flipper layouts. 
			//Charts loads when user flips, use the loading animation/image
			//Only two layouts inside the flipper.
		
		//Load first interpretation
		//Object chart = null;
		//loadInterpretation(chart);
		
		//On change, remember to clear textedit
	}
	
	//Activity Methods\\
	void update() {
		//Contacts server to get current interpretations
		UpdateDaemon.getDaemon().update();
		
		//Gets list from UpdateDaemon
		
		//Testing
		mNumberOfInterpretations = 1;
		

		//test = new TextView(null);
		//mLayoutInterpretations.addView(test);
		//test.setText(mNumberOfInterpretations);
		
		
		/*
		NamedNodeMap map = interpretations.item(0).getAttributes();
		/*
		String URLToInterpretation = ConnectionManager.getConnectionManager().doRequest(map.getNamedItem("href").getNodeValue() + ".xml");
		
		NodeList interpretationXML = Util.getDomElement(URLToInterpretation).getChildNodes().item(0).getChildNodes();
		
		TextView temp = new TextView(null);
		
		//Get thread starter
		for(int i = 0; i < interpretationXML.getLength(); i++)
		{
			if(interpretationXML.item(i).getNodeName().equals("text"))
			{
				temp.setText(interpretationXML.item(i).getChildNodes().item(0).getNodeValue());				
			}

		}
		mLayoutInterpretations.addView(temp);
		*/
		
		/* TODO: needs a quick fix
		//Get comments after thread starter
		for(int i = 0; i < interpretationXML.getLength(); i++)
		{
			if(interpretationXML.item(i).getNodeName().equals("comments"))
			{
				NodeList comments = interpretationXML.item(i).getChildNodes();
				
				for(int j = 0; j < comments.getLength(); j++)
				{
					tempString = comments.item(i).getNodeValue();
					mInterpretations.setText(tempString);
				}
			}
		}
		*/
	}
	
	void refresh()
	{
		
	}
	
	void addImageToView(String url, int imageView) {
		String interpretation = ConnectionManager.getConnectionManager().doRequest(url + ".xml");
		NodeList nodes = Util.getDomElement(interpretation).getChildNodes().item(0).getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("chart")) {
				ImageView image = new ImageView(this);
				image.setImageDrawable(ConnectionManager.getConnectionManager()
						.getImage(nodes.item(i).getAttributes().getNamedItem("href").getNodeValue() 
								+ "/data"));
				
				mLayoutChart.addView(image);
				return;
				
			}
		}
		
		
	}

	void setActivityObjects()
	{		
		mSpnInterpretationList = (Spinner) findViewById(R.id.interpretations_spinner);
		mViewFlipper = (ViewFlipper) findViewById(R.id.interpretations_flipper);
		mLayoutChart = (LinearLayout) findViewById(R.id.interpretations_imageContainer1);
		mLayoutInterpretations = (LinearLayout) findViewById(R.id.interpretations_commentContainer1);
		mEditTextInterpretation = (EditText) findViewById(R.id.interpretation_editAddInterpretation);
		mButtonAddInterpretation = (Button) findViewById(R.id.interpretation_btnAdd);
		mButtonAddInterpretation.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
			}
		});
		mButtonRefresh = (Button) findViewById(R.id.interpretation_btnRefresh);
		mButtonRefresh.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				update();				
			}
		});
		
	}
	
	//Sets the image and creates textViews for each comment
	//dataSet used to define which interpretation it takes data from
	void setData(int dataSet)
	{
		TextView tempText = null;
		
		if(mViewFlipper.getDisplayedChild() == 0)
		{
			//TODO
			for(int i = 0; i < 1; i++) //Loop that goes through the list 
			{
				//Get comment text
			}
		}
		else
		{
			
		}
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	mPrevXtouchValue = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                float currentXtouchValue = touchevent.getX();
                if (mPrevXtouchValue < currentXtouchValue)
                {
                	mViewFlipper.setInAnimation(this, R.anim.in_from_left);
                	mViewFlipper.setOutAnimation(this, R.anim.out_to_right);
                	mViewFlipper.showNext();
                }
                if (mPrevXtouchValue > currentXtouchValue)
                {
                    mViewFlipper.setInAnimation(this, R.anim.in_from_right);
                    mViewFlipper.setOutAnimation(this, R.anim.out_to_left);
                    mViewFlipper.showPrevious();
                    
                }
                break;
            }
        }
        return false;
    }
	
}
