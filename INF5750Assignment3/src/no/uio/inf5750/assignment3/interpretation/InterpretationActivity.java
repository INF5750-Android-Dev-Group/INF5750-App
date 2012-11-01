package no.uio.inf5750.assignment3.interpretation;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.uio.inf5750.assignment3.ConnectionManager;
import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import no.uio.inf5750.assignment3.Util;
import android.app.Activity;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.util.Log;
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
			if (i == 1) {
				break;
			}
		}
	}
	
	void addImageToView(String url) {
		String interpretation = ConnectionManager.getConnectionManager().doRequest(url + ".xml");
		NodeList nodes = Util.getDomElement(interpretation).getChildNodes().item(0).getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("chart")) {
				ImageView image = new ImageView(this);
				image.setImageDrawable(ConnectionManager.getConnectionManager()
						.getImage(nodes.item(i).getAttributes().getNamedItem("href").getNodeValue() 
								+ "/data"));
				
				mLayout.addView(image);
				return;
				
			}
		}
		
		
	}
}
