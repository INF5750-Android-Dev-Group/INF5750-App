package no.uio.inf5750.assignment3;

import no.uio.inf5750.assignment3.diagram.DiagramActivity;
import no.uio.inf5750.assignment3.login.LoginActivity;
import no.uio.inf5750.assignment3.map.MapActivity;
import no.uio.inf5750.assignment3.messaging.MessagingActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button mButtonDiagram;
	private Button mButtonMap;
	private Button mButtonMessaging;
	
	private Context mContext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Look in res/layout/ for the layout definitions.
        
        mContext = this;
        
        setButtons();
	Intent intent = new Intent(mContext, LoginActivity.class);
	startActivity(intent);

    }
    
    public void setButtons()
    {
        mButtonDiagram = (Button) findViewById(R.id.main_button_diagram);
        mButtonMap = (Button) findViewById(R.id.main_button_map);
        mButtonMessaging = (Button) findViewById(R.id.main_button_messaging);
    	
    	mButtonDiagram.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, DiagramActivity.class);
				startActivity(intent);
				
			}
        	
        });
        
        mButtonMap.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, MapActivity.class);
				startActivity(intent);
				
			}
        	
        });
        
        mButtonMessaging.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext, MessagingActivity.class);
				startActivity(intent);
				
			}
        	
        });
    }
}