package no.uio.inf5750.assignment3.interpretation;

import java.util.ArrayList;
import java.util.LinkedList;

import org.w3c.dom.Document;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.util.ConnectionManager;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import no.uio.inf5750.assignment3.util.Util;
import no.uio.inf5750.assignment3.util.Interpretation;
import android.app.Activity;
import android.content.pm.ApplicationInfo;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class InterpretationActivity extends Activity {
	private Spinner mSpnInterpretationList;
	private LinearLayout mLayoutInterpretations;
	private ImageView mImageContainer;
	private EditText mEditTextInterpretation;
	private Button mButtonAddInterpretation, mButtonRefresh;
	
	private float mPrevXtouchValue;
	private int mCurrentInterpretation;
	private LinkedList<Interpretation> mInterpretationList;
	private String[] mInterpretationNameList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interpretations_flipper);
		
		//Initializes variables
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
	private void update() {
		//Contacts server to get current interpretations
		UpdateDaemon.getDaemon().update();
		
		
		//If there are no interpretations there is no need to do anything.
		if(UpdateDaemon.getDaemon().getNumberOfInterpretations() < 1) 
		{
			mInterpretationNameList = new String[]{"No interpretations.."};
		}
		else
		{
			//Gets list off interpretations from UpdateDaemon
			mInterpretationList = UpdateDaemon.getDaemon().getInterpretations();
		
			//Fills the spinner with the name of each dataset that has comments
					
			LinkedList<String> tempList = new LinkedList<String>();
			for(int i = 0; i < mInterpretationList.size(); i++)
			{
				if(!mInterpretationList.get(i).mInfo.isEmpty()) {
					if(mInterpretationList.get(i).mInfo.get(0).mName == null)
					{
						System.out.println("its null");
					}
					else
					{
						tempList.add(mInterpretationList.get(i).mInfo.get(0).mName);
					}
				}
			}
			mInterpretationNameList = tempList.toArray(new String[tempList.size()]);
		}
		
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, R.layout.spinner_header, mInterpretationNameList);//ArrayAdapter.createFromResource(this, R.array.game_speeds, R.layout.spinner_header);
    	spinner_adapter.setDropDownViewResource(R.layout.spinner_list_item);
    	mSpnInterpretationList.setAdapter(spinner_adapter);
    	mSpnInterpretationList.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				mSpnInterpretationList.setSelection(arg2);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			
		//Creates a TextView that shows interpretation
		//showInitialInterpretation(mCurrentInterpretation);
		
		//Creates a TextView for each comment and adds it into the layout
		showComments(mCurrentInterpretation);
		
		//Load image/chart into the placeholder
		//showChart(mCurrentInterpretation);
		
		//text = mInterpretationList[0].mId;
		
		//text = ConnectionManager.getConnectionManager().getSite() + "charts/" + text + "/name";
		
		//TextView test = (TextView) findViewById(R.id.interpretationsTextView1);
		//test.setText(text);
		
		
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
	
	private void showInitialInterpretation(int interpretation)
	{
		
	}
	
	private void showComments(int interNr)
	{
		mLayoutInterpretations.removeAllViews();
		
		for(int i = 0; i < mInterpretationList.get(interNr).mCommentThread.size(); i++)
		{
			TextView tempView = new TextView(this);
			tempView.setText(mInterpretationList.get(interNr).mCommentThread.get(i).mText);
			mLayoutInterpretations.addView(tempView);
			Log.d("Testing", mInterpretationList.get(interNr).mCommentThread.get(i).mText);
		}
	}
	
	private void showChart(String url, int imageView)
	{
		String interpretation = ConnectionManager.getConnectionManager().doRequest(url + ".xml");
		NodeList nodes = Util.getDomElement(interpretation).getChildNodes().item(0).getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("chart")) {
				mImageContainer.setImageDrawable(ConnectionManager.getConnectionManager()
						.getImage(nodes.item(i).getAttributes().getNamedItem("href").getNodeValue() 
								+ "/data"));
				
				return;
				
			}
		}
	}

	void setActivityObjects()
	{		
		mSpnInterpretationList = (Spinner) findViewById(R.id.interpretations_spinner);
		mImageContainer = (ImageView) findViewById(R.id.interpretations_imageContainer);
		mLayoutInterpretations = (LinearLayout) findViewById(R.id.interpretations_commentContainer);
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
}
