package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.interpretation.InterpretationActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity{
	/** Called when the activity is first created. */
	String mMessage = "";
	private Button buttonUnread;
	private Button buttonRemove;
	private Button buttonBack;
	private Button buttonUnread2;
	private Button buttonRemove2;
	private Button buttonBack2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mMessage = extras.getString("message");
			}
		} else {
			mMessage = savedInstanceState.getString("message");
		}
		
		//Add the buttons and their actions
		setButtons();
		
		showMessage();
	}
	
	public void setButtons()
	{
		//Create button by id
		buttonUnread = (Button) findViewById(R.id.message_unread_button);
		buttonRemove = (Button) findViewById(R.id.message_remove_button);
		buttonBack = (Button) findViewById(R.id.message_back_button);
		buttonUnread2 = (Button) findViewById(R.id.message_unread2_button);
		buttonRemove2 = (Button) findViewById(R.id.message_remove2_button);
		buttonBack2 = (Button) findViewById(R.id.message_back2_button);
		
		//Set text
		buttonUnread.setText(getString(R.string.message_unread_button));
		buttonRemove.setText(getString(R.string.message_remove_button));
		buttonBack.setText(getString(R.string.message_back_button));
		buttonUnread2.setText(getString(R.string.message_unread_button));
		buttonRemove2.setText(getString(R.string.message_remove_button));
		buttonBack2.setText(getString(R.string.message_back_button));
		
		//Set onclicklistener
		buttonUnread.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				unread();
			}
		});
		
		buttonRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				remove();
			}
		});
		
		buttonBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});
		
		buttonUnread2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				unread();
			}
		});
		
		buttonRemove2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				remove();
			}
		});
		
		buttonBack2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});
	}
	
	public void unread(){
		Toast toast = Toast.makeText(MessageActivity.this,"Mark message as unread", Toast.LENGTH_LONG);  
        toast.show();
	}
	
	public void remove(){
		Toast toast = Toast.makeText(MessageActivity.this,"Remove message", Toast.LENGTH_LONG);  
        toast.show();
	}
	
	public void back(){
		Intent intent = new Intent(MessageActivity.this, MessagingActivity.class);
		startActivity(intent);
	}
	
	public void showMessage() {
		TextView title = (TextView) findViewById(R.id.message_title);
		title.setText(mMessage);
	}

}
