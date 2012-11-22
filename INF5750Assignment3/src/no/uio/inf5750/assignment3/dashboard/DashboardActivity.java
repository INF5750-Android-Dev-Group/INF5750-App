package no.uio.inf5750.assignment3.dashboard;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.interpretation.InterpretationActivity;
import no.uio.inf5750.assignment3.messaging.MessagingActivity;
import no.uio.inf5750.assignment3.util.ConnectionManager;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DashboardActivity extends Activity {

	private ProgressDialog mProgressDialog;
	
	private Button mButtonMessaging;
	private Button mButtonInterpretation;
	
	private ImageView mImageView1, mImageView2;

	private Context mContext;

	private String mOutput;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Look in res/layout/ for the layout definitions.

		mContext = this;

		setButtons();

		mOutput = "";

		mImageView1 = (ImageView) findViewById(R.id.main_imageview1);
		mImageView2 = (ImageView) findViewById(R.id.main_imageview2);
		
		setImages();

		
		//Old data fetching stuff..
		//String response = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/interpretations/cjG99uolq7c.xml");
		//String response = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/messageConversations.xml");	
		//print(response);
		//print(ConnectionManager.getConnectionManager().getLog());

	}
	
	Drawable drawable1;
	Drawable drawable2;
	public void setImages()
	{ //fetching data in a thread for making things appear smoother
		
		mProgressDialog = ProgressDialog.show(mContext, "Loading", "Please wait..");
		
		final Thread setImageThread = new Thread(){
			
			public void run()
			{
				updateButtons();
				
				if(drawable1!=null)
		        	mImageView1.setImageDrawable(drawable1);
		        
		        if(drawable2!=null)
		        	mImageView2.setImageDrawable(drawable2);
		        
		        if(mProgressDialog!=null)
		        	mProgressDialog.dismiss();
			}
		};

		
		Thread getImageThread = new Thread()
		{
			public void run()
			{
				UpdateDaemon.getDaemon().update();
				drawable1 = ConnectionManager.getConnectionManager().getImage("http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data");
				drawable2 = ConnectionManager.getConnectionManager().getImage("http://apps.dhis2.org/demo/api/charts/CiooTWsT3AP/data");
		        
				runOnUiThread(setImageThread);
			}
		};
		getImageThread.start();	
		
	}
	
	public void updateButtons()
	{
		mButtonMessaging.setText(getString(R.string.launch_messaging) + " (" + UpdateDaemon.getDaemon().getUnreadMessages() + ")");
	}

	public void setButtons()
	{
		
		mButtonMessaging = (Button) findViewById(R.id.main_button_messaging);
		mButtonInterpretation = (Button) findViewById(R.id.main_button_interpretation);
		mButtonMessaging.setText(getString(R.string.launch_messaging));
		mButtonMessaging.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				Intent intent = new Intent(mContext, MessagingActivity.class);
				startActivity(intent);

			}

		});
		
		mButtonInterpretation.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				Intent intent = new Intent(mContext, InterpretationActivity.class);
				startActivity(intent);

			}

		});
	}
	
	/*public void print(String output)
	{
		mOutput += output + "\n\n";
		mTextView.setText(mOutput);
	}*/
}
