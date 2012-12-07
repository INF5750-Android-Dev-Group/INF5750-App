package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import no.uio.inf5750.assignment3.util.ConnectionManager;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {
	
	// Not sure if we'll need this
	String mMessage = "";
	int mIndex;
	private Button buttonRemove;
	private Button buttonUnread;
	private Button buttonReply;

	/** 
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				mMessage = extras.getString("message");
				mIndex = extras.getInt("messageIndex");
			}
		} else {
			mMessage = savedInstanceState.getString("message");
			mIndex = savedInstanceState.getInt("messageIndex");
		}

		// Add the buttons and their actions
		setButtons();

		showMessage();
	}

	/**
	 * Initialize button listener for message activity
	 */
	public void setButtons() {
		// Create button by id
		buttonReply = (Button) findViewById(R.id.message_reply_button);

		// Set text
		buttonReply.setText(getString(R.string.message_reply_button));

		// Set onclicklistener
		buttonReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				launchReplyDialog();
			}
		});
	}
	
	/**
	 * Launches a reply dialog for the message in focus
	 */
	public void launchReplyDialog()
	{
		AlertDialog.Builder builder;
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.replymessage_dialog, (ViewGroup) findViewById(R.id.layout_root));
		builder = new AlertDialog.Builder(this);
    	builder.setView(layout);
		final AlertDialog dialog = builder.create();
		final EditText editText = (EditText) layout.findViewById(R.id.replydialog_edittext);
		Button button = (Button) layout.findViewById(R.id.replydialog_replybutton);
    	button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String replyText = editText.getText().toString();
				dialog.dismiss();
				ConnectionManager.getConnectionManager().
					replyMessage(mIndex, replyText);
				UpdateDaemon.getDaemon().update();
				showMessage();
				
			}
		});
		dialog.show();
	}
	
	/**
	 * Will show the chosen message
	 */
	@SuppressWarnings("unchecked")
	public void showMessage() {
		String messageTitle = UpdateDaemon.getDaemon().getMessage(mIndex).mTitle;
		TextView title = (TextView) findViewById(R.id.message_title);
		title.setText(messageTitle);
		LinkedHashMap<String, String> info = UpdateDaemon.getDaemon()
				.getMessage((String) ("http://apps.dhis2.org/dev/api/messageConversations/"+mMessage + ".xml"));
		title.setText(info.get("title"));
		info.remove("title");
		String[] values = new String[info.size() / 3];
		// Get a set of the entries and iterator
		Set set = info.entrySet();
		Iterator i = set.iterator();
		Map.Entry<String, String> me;
		int counter = 0;
		while (i.hasNext()) {
			me = (Map.Entry<String, String>) i.next();
			String text = me.getValue().toString();
			me = (Map.Entry<String, String>) i.next();
			String date = me.getValue().toString();
			me = (Map.Entry<String, String>) i.next();
			String sender = me.getValue().toString();

			values[counter++] = sender + "\t" + date + "\n\n" + text;
		}

		ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_item,
				values);
		ListView mListView = (ListView) findViewById(R.id.message_listview);
		mListView.setAdapter(arrayAdapter);
	}

}
