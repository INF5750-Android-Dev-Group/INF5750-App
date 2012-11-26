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
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ViewSwitcher;

public class InterpretationActivity extends Activity {
	private Spinner mSpnInterpretationList;
	private ViewSwitcher mResourceSwitcher;
	private LinearLayout mCommentsContainer, mResourceContainer;
	private ImageView mImageContainer;
	private TextView mInitialComment, mUserDateInfo, mResourceInformer;
	private EditText mEditTextInterpretation;
	private Button mButtonAddInterpretation, mButtonRefresh;
	
	private float mPrevXtouchValue;
	private int mCurrentInterpretation;
	private LinkedList<Interpretation> mInterpretationList;
	private String[] mInterpretationNameList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interpretations);
		
		//Initializes variables
		mCurrentInterpretation = 0;
		setActivityObjects();
		
		//Updates interpretation list
		update();
	}
	
	//Activity Methods\\
	private void update() {
		//Contacts server to get current interpretations
		UpdateDaemon.getDaemon().update();
		
		//If there are no interpretations there is no need to do anything.
		if(UpdateDaemon.getDaemon().getNumberOfInterpretations() < 1) return;
		
		//Gets list off interpretations from UpdateDaemon
		mInterpretationList = UpdateDaemon.getDaemon().getInterpretations();
	
		//Fills the spinner with the name of each dataset that has comments
		mInterpretationNameList = new String[mInterpretationList.size()];		
		for(int i = 0; i < mInterpretationList.size(); i++)
		{
			if(!mInterpretationList.get(i).mInfo.isEmpty()) {
				mInterpretationNameList[i] = mInterpretationList.get(i).mInfo.get(0).mName;
			}
		}
		
		//TODO - Simen spinner fiks
		ArrayAdapter<String> mSpnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mInterpretationNameList);
		mSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnInterpretationList.setAdapter(mSpnAdapter);
		
		//Updates the content
		showInterpretation(mCurrentInterpretation);
	}
	
	private void showInterpretation(int interNr)
	{
		//Removes previous content
		mCommentsContainer.removeAllViews();
		mResourceContainer.removeAllViews();
		//mSpnInterpretationList.setSelection(interNr);
		
		//Creates a TextView that shows initial interpretation
		showInitialComment(interNr);
		
		//Load image/chart into the placeholder
		showChart(interNr);
		
		//Creates a TextView for each comment and adds it into the layout
		showComments(interNr);	
	}
	
	private void showInitialComment(int interNr)
	{
		String info = mInterpretationList.get(interNr).mUser.mName + ": ";
		mUserDateInfo.setText(info);
		mUserDateInfo.setTextColor(getResources().getColor(R.color.Pink));
		String text = mInterpretationList.get(interNr).mText;
		mInitialComment.setText(text);
	}
	
	private void showComments(int interNr)
	{
		for(int i = 0; i < mInterpretationList.get(interNr).mCommentThread.size(); i++)
		{
			TextView tempView1 = new TextView(this);
			tempView1.setText(mInterpretationList.get(interNr).mCommentThread.get(i).mUser.mName + ": ");
			tempView1.setTextColor(getResources().getColor(R.color.Pink));
			mCommentsContainer.addView(tempView1);
			
			TextView tempView2 = new TextView(this);
			tempView2.setText(mInterpretationList.get(interNr).mCommentThread.get(i).mText);
			mCommentsContainer.addView(tempView2);
			
			Log.d("Log Comment Text:", mInterpretationList.get(interNr).mCommentThread.get(i).mText);
		}
	}
	
	private void showChart(int InterNr)
	{
		//If there is no resource associated with the interpretation
		if(mInterpretationList.get(InterNr).mInfo.size() < 1)
		{ 
			TextView tempView = new TextView(this);
			tempView.setText("No resource associated with interpretation");
			mResourceContainer.addView(tempView);
			//mResourceInformer.setText("No resource associated with interpretation");
			//if(mResourceSwitcher.getDisplayedChild() == 0)mResourceSwitcher.showNext();
			return;
		}
		
		//If there is a link to the associated resource
		if(!mInterpretationList.get(InterNr).mInfo.get(0).mChart)
		{
			TextView tempView = new TextView(this);
			tempView.setText("TODO: create a link");
			mResourceContainer.addView(tempView);
			//mResourceInformer.setText("TODO: create a link");
			//if(mResourceSwitcher.getDisplayedChild() == 0)mResourceSwitcher.showNext();
			return;
		}
		//If there is a chart to be drawn from the resource
		else
		{
			Drawable drawable = ConnectionManager.getConnectionManager().getImage(mInterpretationList.get(InterNr).mInfo.get(0).getImageUrl());
			ImageView tempView = new ImageView(this);
			tempView.setImageDrawable(drawable);
			mResourceContainer.addView(tempView);
			//mImageContainer.setImageDrawable(drawable);
			//if(mResourceSwitcher.getDisplayedChild() == 1)mResourceSwitcher.showPrevious();
		}
	}
	
	void setActivityObjects()
	{		
		mSpnInterpretationList = (Spinner) findViewById(R.id.interpretations_spinner);
		mResourceContainer = (LinearLayout) findViewById(R.id.interpretations_resourceContainer);
		//mResourceSwitcher = (ViewSwitcher) findViewById(R.id.interpretation_resourceSwitcher);
		mImageContainer = (ImageView) findViewById(R.id.interpretations_imageContainer);
		mResourceInformer = (TextView) findViewById(R.id.interpretations_resourceText);
		mInitialComment = (TextView) findViewById(R.id.interpretations_initCommentText);
		mUserDateInfo = (TextView) findViewById(R.id.interpretations_initCommentUserDate);
		mCommentsContainer = (LinearLayout) findViewById(R.id.interpretations_commentContainer);
		mEditTextInterpretation = (EditText) findViewById(R.id.interpretation_editAddInterpretation);
		mButtonAddInterpretation = (Button) findViewById(R.id.interpretation_btnAdd);
		mButtonAddInterpretation.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				mCurrentInterpretation += 1;
				showInterpretation(mCurrentInterpretation);
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
