package no.uio.inf5750.assignment3.interpretation;

import java.util.LinkedList;
import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.util.ConnectionManager;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import no.uio.inf5750.assignment3.util.Interpretation;
import android.app.Activity;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
	private LinearLayout mCommentsContainer, mResourceContainer;
	private TextView mInitialComment, mUserDateInfo;
	private EditText mEditTextInterpretation;
	private Button mButtonAddInterpretation, mButtonRefresh;
	
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
	
		//Creates a list of the resources with interpretations
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
		
		//Populates spinner and sets listener
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this, R.layout.spinner_header, mInterpretationNameList);//ArrayAdapter.createFromResource(this, R.array.game_speeds, R.layout.spinner_header);
    	spinner_adapter.setDropDownViewResource(R.layout.spinner_list_item);
    	mSpnInterpretationList.setAdapter(spinner_adapter);
    	mSpnInterpretationList.setOnItemSelectedListener(new OnItemSelectedListener() 
    	{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				mCurrentInterpretation = arg2;
				showInterpretation(arg2);
				mSpnInterpretationList.setSelection(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
			}
		});
		
    	//Shows the current interpretation and associated resources
    	showInterpretation(mCurrentInterpretation);
	}
	
	private void showInterpretation(int interNr)
	{
		//Do not show interpretations if there are none to show 
    	if(UpdateDaemon.getDaemon().getNumberOfInterpretations() < 1) return;
    	
		//Removes previous content
		mCommentsContainer.removeAllViews();
		mResourceContainer.removeAllViews();
		mSpnInterpretationList.setSelection(interNr);
		
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
			return;
		}
		
		//If there is a link to the associated resource
		if(!mInterpretationList.get(InterNr).mInfo.get(0).mChart)
		{
			TextView tempView = new TextView(this);
			tempView.setText("TODO: create a link");
			mResourceContainer.addView(tempView);
			return;
		}
		//If there is a chart to be drawn from the resource
		else
		{
			Drawable drawable = ConnectionManager.getConnectionManager().getImage(mInterpretationList.get(InterNr).mInfo.get(0).getImageUrl());
			ImageView tempView = new ImageView(this);
			tempView.setImageDrawable(drawable);
			LayoutParams tempParamp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			System.out.println(tempParamp.toString());
			tempView.setLayoutParams(tempParamp);
			tempView.setAdjustViewBounds(true);
			mResourceContainer.addView(tempView);
		}
	}
	
	private void addInterpretation(int interNr)
	{
		if(mEditTextInterpretation.getText().toString().matches("")) return;
		TextView tempView1 = new TextView(this);
		//Get current user?
		tempView1.setTextColor(getResources().getColor(R.color.Pink));
		mCommentsContainer.addView(tempView1);
		
		TextView tempView2 = new TextView(this);
		tempView2.setText(mEditTextInterpretation.getText());
		mCommentsContainer.addView(tempView2);
		
		mEditTextInterpretation.setText("");
	}
	
	void setActivityObjects()
	{		
		mSpnInterpretationList = (Spinner) findViewById(R.id.interpretations_spinner);
		mResourceContainer = (LinearLayout) findViewById(R.id.interpretations_resourceContainer);
		mInitialComment = (TextView) findViewById(R.id.interpretations_initCommentText);
		mUserDateInfo = (TextView) findViewById(R.id.interpretations_initCommentUserDate);
		mCommentsContainer = (LinearLayout) findViewById(R.id.interpretations_commentContainer);
		mEditTextInterpretation = (EditText) findViewById(R.id.interpretation_editAddInterpretation);
		mButtonAddInterpretation = (Button) findViewById(R.id.interpretation_btnAdd);
		mButtonAddInterpretation.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				addInterpretation(mCurrentInterpretation);
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
