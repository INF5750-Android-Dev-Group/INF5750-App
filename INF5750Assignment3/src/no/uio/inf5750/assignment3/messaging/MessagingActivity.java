package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.ConnectionManager;
import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import no.uio.inf5750.assignment3.R.id;
import no.uio.inf5750.assignment3.R.layout;
import no.uio.inf5750.assignment3.Util;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagingActivity extends Activity {
	/** Called when the activity is first created. */
	String mMessages = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messaging);
		update();
	}
	public void update() {
		UpdateDaemon.getUpdateDaemon().update();
		NodeList messages = UpdateDaemon.getUpdateDaemon().getMessages();
		LinearLayout msgList = (LinearLayout) findViewById(R.id.message_list);
		if (messages != null) {
			for (int i = 0; i < messages.getLength(); i++) {
				TextView text = new TextView(this);
				NamedNodeMap map = messages.item(i).getAttributes();
				text.setText(map.getNamedItem("name").getNodeValue());
				msgList.addView(text);
			}
		} 
	}
}
