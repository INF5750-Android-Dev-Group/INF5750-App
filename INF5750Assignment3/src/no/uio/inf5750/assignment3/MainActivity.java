package no.uio.inf5750.assignment3;

import no.uio.inf5750.assignment3.diagram.DiagramActivity;
import no.uio.inf5750.assignment3.interpretation.InterpretationActivity;
import no.uio.inf5750.assignment3.login.LoginActivity;
import no.uio.inf5750.assignment3.map.MapActivity;
import no.uio.inf5750.assignment3.messaging.MessagingActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button mButtonDiagram;
	private Button mButtonMap;
	private Button mButtonMessaging;
	private Button mButtonInterpretation;

	private Context mContext;
	
	private TextView mTextView;
	private String mOutput;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Look in res/layout/ for the layout definitions.

		mContext = this;

		setButtons();
		Intent intent = new Intent(mContext, LoginActivity.class);
		startActivity(intent);

		mOutput = "";
		mTextView = (TextView) findViewById(R.id.main_infotext);

		//String response = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/interpretations/cjG99uolq7c.xml");
		String response = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/messageConversations.xml");
		
		print(response);

		print(ConnectionManager.getConnectionManager().getLog());

		//getValue()..
		//getElementValue()..

	}

	public void setButtons()
	{
		mButtonDiagram = (Button) findViewById(R.id.main_button_diagram);
		mButtonMap = (Button) findViewById(R.id.main_button_map);
		mButtonMessaging = (Button) findViewById(R.id.main_button_messaging);
		mButtonInterpretation = (Button) findViewById(R.id.main_button_interpretation);
		
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
		
		mButtonInterpretation.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(mContext, InterpretationActivity.class);
				startActivity(intent);

			}

		});
	}
	public void print(String output)
	{
		mOutput += output + "\n\n";
		mTextView.setText(mOutput);
	}
}
