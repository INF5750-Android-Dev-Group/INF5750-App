package no.uio.inf5750.assignment3.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import no.uio.inf5750.assignment3.R;

public class LoginActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button loginButton = (Button) findViewById(R.id.login_button);

		loginButton.setOnClickListener(new LoginOnClickListener());

	}
	class LoginOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			EditText username = (EditText) findViewById(R.id.username);
			EditText password = (EditText) findViewById(R.id.password);
		}

	}
}
