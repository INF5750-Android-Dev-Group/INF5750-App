package no.uio.inf5750.assignment3;

import org.w3c.dom.Document;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        String messageList = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/messageConversations.xml");
        /*Document doc = Util.getDomElement(messageList);
        NodeList list = doc.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
        }*/
        LinearLayout list = (LinearLayout) findViewById(R.id.message_list);
        TextView text = new TextView(this);
        text.setText("test");
        list.addView(text);
    }
    
}
