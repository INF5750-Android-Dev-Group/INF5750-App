package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessagingActivity extends Activity {
	/** Called when the activity is first created. */

	private ListView mListView;
	private Context mContext;
	String mMessages = "";
	private Button mButtonMessage;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging);
		mContext = this;
		setButton();
		setupViews();
		update();
	}

	public void setupViews()
	{
		mListView = (ListView) findViewById(R.id.messaging_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText().toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void update() {
		UpdateDaemon.getDaemon().update();
		NodeList messages = UpdateDaemon.getDaemon().getMessages();
		LinearLayout layout = (LinearLayout) findViewById(R.id.message_list);
		layout.removeAllViews();
		if (messages != null) {

			String[] values = new String[messages.getLength()];

			for (int i = 0; i < messages.getLength(); i++) {
				TextView text = new TextView(this);
				NamedNodeMap map = messages.item(i).getAttributes();
				text.setText(map.getNamedItem("name").getNodeValue());
				layout.addView(text);
				values[i] = messages.item(i).getAttributes().getNamedItem("name").getNodeValue();
			}

			setListView(values);
		} 
	}

	public void setButton()
	{
		mButtonMessage = (Button) findViewById(R.id.show_message_button);
		mButtonMessage.setText("Show message");
		mButtonMessage.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent(mContext, MessageActivity.class);
				startActivity(intent);

			}

		});
	}

	public void setListView(String[] values)
	{
		//ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, R.layout.list_item, values);
		//mListView.setAdapter(arrayAdapter);
	}
}