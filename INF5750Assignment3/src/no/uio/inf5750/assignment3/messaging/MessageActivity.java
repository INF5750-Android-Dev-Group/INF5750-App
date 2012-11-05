package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageActivity extends Activity{
	/** Called when the activity is first created. */
	String mMessage = "";
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
		showMessage();
	}
	
	public void showMessage() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.message_layout);
		TextView text = new TextView(this);
		text.setText(mMessage);
		layout.addView(text);
	}

}
