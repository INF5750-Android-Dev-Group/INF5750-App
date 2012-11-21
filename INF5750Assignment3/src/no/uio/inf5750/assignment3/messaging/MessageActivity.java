package no.uio.inf5750.assignment3.messaging;
import no.uio.inf5750.assignment3.R;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.uio.inf5750.assignment3.interpretation.InterpretationActivity;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {
	/** Called when the activity is first created. */
	// Not sure if we'll need this
	String mMessage = "";
	int mIndex;
	private Button buttonRemove;
	private Button buttonRemove2;
	private Button buttonUnread;
	private Button buttonUnread2;
	private Button buttonReply;

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

	public void setButtons() {
		// Create button by id
		buttonUnread = (Button) findViewById(R.id.message_unread_button);
		buttonRemove = (Button) findViewById(R.id.message_remove_button);
		buttonUnread2 = (Button) findViewById(R.id.message_unread2_button);
		buttonRemove2 = (Button) findViewById(R.id.message_remove2_button);
		buttonReply = (Button) findViewById(R.id.message_reply_button);

		// Set text
		buttonUnread.setText(getString(R.string.message_unread_button));
		buttonRemove.setText(getString(R.string.message_remove_button));
		buttonUnread2.setText(getString(R.string.message_unread_button));
		buttonRemove2.setText(getString(R.string.message_remove_button));
		buttonReply.setText(getString(R.string.message_reply_button));

		// Set onclicklistener
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

		buttonReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast toast = Toast.makeText(MessageActivity.this,
						"Reply message", Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}

	public void unread() {
		Toast toast = Toast.makeText(MessageActivity.this,
				"Mark message as unread", Toast.LENGTH_LONG);
		toast.show();
	}

	public void remove() {
		Toast toast = Toast.makeText(MessageActivity.this, "Remove message",
				Toast.LENGTH_LONG);
		toast.show();
	}

	public void back() {
		Intent intent = new Intent(MessageActivity.this,
				MessagingActivity.class);
		startActivity(intent);
	}

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
			Log.d("KEY", me.getKey().toString());
			Log.d("Value", me.getValue().toString());
		}

		ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_item,
				values);
		ListView mListView = (ListView) findViewById(R.id.message_listview);
		mListView.setAdapter(arrayAdapter);
	}

}
