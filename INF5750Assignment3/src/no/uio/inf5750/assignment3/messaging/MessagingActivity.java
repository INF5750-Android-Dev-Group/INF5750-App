package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		UpdateDaemon.getDaemon().update();
		NodeList messages = UpdateDaemon.getDaemon().getMessages();
		LinearLayout layout = (LinearLayout) findViewById(R.id.message_list);
		layout.removeAllViews();
		if (messages != null) {
			for (int i = 0; i < messages.getLength(); i++) {
				TextView text = new TextView(this);
				NamedNodeMap map = messages.item(i).getAttributes();
				text.setText(map.getNamedItem("name").getNodeValue());
				layout.addView(text);
			}
		} 
	}
}
