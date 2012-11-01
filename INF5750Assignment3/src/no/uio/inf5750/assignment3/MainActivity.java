package no.uio.inf5750.assignment3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private Context mContext;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		mContext = this;
		mActivity = this;

		Button loginButton = (Button) findViewById(R.id.login_button);
		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.password);
		username.setText("admin");
		password.setText("district");

		loginButton.setOnClickListener(new LoginOnClickListener());

	}
	class LoginOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			
			
			Intent intent = new Intent(mContext, DashboardActivity.class);
			startActivity(intent);
			
			mActivity.finish();
		}

	}
}
