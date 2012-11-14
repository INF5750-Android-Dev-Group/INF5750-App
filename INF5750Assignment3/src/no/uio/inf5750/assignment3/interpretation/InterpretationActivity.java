package no.uio.inf5750.assignment3.interpretation;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import no.uio.inf5750.assignment3.ConnectionManager;
import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.UpdateDaemon;
import no.uio.inf5750.assignment3.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InterpretationActivity extends Activity {
	private LinearLayout mImageLayout, mLayout;
	private ImageView mImageContainer;
	private Button mButtonAddInterpretation, mButtonRefreshChart;
	private TextView mInterpretations;
	private EditText mEditTextInterpretation;
	
	ConnectionManager connectionManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interpretation);
		
		
		setActivityObjects();
		update();
	}
	
	//Activity Methods\\
	void update() {
		String tempString = "";
		
		NodeList interpretations = UpdateDaemon.getDaemon().getInterpretationList();
		NamedNodeMap map = interpretations.item(0).getAttributes();
		
		String URLToInterpretation = ConnectionManager.getConnectionManager().doRequest(map.getNamedItem("href").getNodeValue() + ".xml");
		
		NodeList interpretationXML = Util.getDomElement(URLToInterpretation).getChildNodes().item(0).getChildNodes();
		
		
		//Get thread starter
		for(int i = 0; i < interpretationXML.getLength(); i++)
		{
			if(interpretationXML.item(i).getNodeName().equals("text"))
			{
				mInterpretations.setText(interpretationXML.item(i).getChildNodes().item(0).getNodeValue());
			}

		}
		
		/* TODO: needs a quick fix
		//Get comments after thread starter
		for(int i = 0; i < interpretationXML.getLength(); i++)
		{
			if(interpretationXML.item(i).getNodeName().equals("comments"))
			{
				NodeList comments = interpretationXML.item(i).getChildNodes();
				
				for(int j = 0; j < comments.getLength(); j++)
				{
					tempString = comments.item(i).getNodeValue();
					mInterpretations.setText(tempString);
				}
			}
		}
		*/
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

	void setActivityObjects()
	{
		mInterpretations = (TextView) findViewById(R.id.txtInterpretations);
		
		mLayout = (LinearLayout) findViewById(R.id.layout_imageContainer);
		
		mButtonAddInterpretation = (Button) findViewById(R.id.btnInterpretation_add);		
		mButtonAddInterpretation.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		mButtonRefreshChart = (Button) findViewById(R.id.btnInterpretation_refresh);
		mButtonRefreshChart.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				update();				
			}
		});
		
	}
}
