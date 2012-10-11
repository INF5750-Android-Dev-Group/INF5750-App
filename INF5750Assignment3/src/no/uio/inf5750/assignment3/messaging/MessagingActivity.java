package no.uio.inf5750.assignment3.messaging;

import no.uio.inf5750.assignment3.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MessagingActivity extends Activity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        
        Toast.makeText(this, "Hello World Messaging!", Toast.LENGTH_LONG).show();
        
    }
    
}
