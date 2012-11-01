package no.uio.inf5750.assignment3.interpretation;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.uio.inf5750.assignment3.ConnectionManager;
import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import android.app.Activity;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InterpretationActivity extends Activity {
	LinearLayout mLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interpretation);
		update();
	}
	
	void update() {
		mLayout = (LinearLayout) findViewById(R.id.interpretation_layout);
		mLayout.removeAllViews();
		NodeList interpretations = UpdateDaemon.getDaemon().getInterpretations();
		for (int i = 0; i < interpretations.getLength(); i++) {
			TextView text = new TextView(this);
			NamedNodeMap map = interpretations.item(i).getAttributes();
			text.setText(map.getNamedItem("href").getNodeValue()+".xml");
			mLayout.addView(text);
			addImageToView(map.getNamedItem("href").getNodeValue());
		}
	}
	
	void addImageToView(String url) {
//		String  = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/messageConversations.xml");
		ImageView image = new ImageView(this);
		image.setImageDrawable(ConnectionManager.getConnectionManager()
				.getImage("http://apps.dhis2.org/dev/api/charts/Emq3LEyWb15/data"));
		mLayout.addView(image);
		
	}
}
