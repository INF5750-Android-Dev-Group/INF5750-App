package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MessagingActivity extends Activity {
	/** Called when the activity is first created. */

	private ListView mListView;
	private Context mContext;
	String mMessages = "";
	private Button mButtonMessage;
	String[] mMessageIDs;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging);
		mContext = this;
		setupViews();
		update();
	}

	public void setupViews()
	{
		mListView = (ListView) findViewById(R.id.messaging_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					Intent intent = new Intent(mContext, MessageActivity.class);
					intent.putExtra("message", mMessageIDs[position]);
					intent.putExtra("messageIndex", position);
					startActivity(intent);
				}
		});
	}

	public void update() {
		UpdateDaemon.getDaemon().update();
		LinearLayout layout = (LinearLayout) findViewById(R.id.message_list);
		layout.removeAllViews();
		int numMessages = UpdateDaemon.getDaemon().getNumberOfMessages();
		String[] values = new String[numMessages];
		mMessageIDs = new String[numMessages];

		for (int i = 0; i < numMessages; i++) {
			values[i] = UpdateDaemon.getDaemon().getMessage(i).mTitle; 
			mMessageIDs[i] = UpdateDaemon.getDaemon().getMessage(i).mId;
		}

		setListView(values);
	}

	public void setListView(String[] values)
	{
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.list_item, values);
		mListView.setAdapter(arrayAdapter);
	}
}
