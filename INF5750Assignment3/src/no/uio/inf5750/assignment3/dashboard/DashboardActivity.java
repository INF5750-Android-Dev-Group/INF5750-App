package no.uio.inf5750.assignment3.dashboard;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.util.ConnectionManager;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DashboardActivity extends Activity {

	//private ProgressDialog mProgressDialog;
	private ProgressBar mProgressBar1, mProgressBar2;
	
	private ImageView mImageView1, mImageView2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mImageView1 = (ImageView) findViewById(R.id.main_imageview1);
		mImageView2 = (ImageView) findViewById(R.id.main_imageview2);
		mProgressBar1 = (ProgressBar) findViewById(R.id.diagram_progress1);
		mProgressBar2 = (ProgressBar) findViewById(R.id.diagram_progress2);
		
		setImages();
	}
	
	Drawable drawable1;
	Drawable drawable2;
	public void setImages()
	{ //fetching data in a thread for making things appear smoother

		final Thread setImageThread1 = new Thread(){
			public void run()
			{//Separate thread to be run on UI bc android demands that
				if(drawable1!=null)
				{
		        	mImageView1.setImageDrawable(drawable1);
				}
				else
				{
					mImageView1.setImageDrawable(getResources().getDrawable(R.drawable.notfound));
				}
				mImageView1.setVisibility(View.VISIBLE);
	        	mProgressBar1.setVisibility(View.INVISIBLE);
			}
		};
		final Thread setImageThread2 = new Thread(){
			public void run()
			{//Separate thread to be run on UI bc android demands that
		        if(drawable2!=null)
		        {
		        	mImageView2.setImageDrawable(drawable2);
		        }
		        else
				{
					mImageView2.setImageDrawable(getResources().getDrawable(R.drawable.notfound));
				}
				mImageView2.setVisibility(View.VISIBLE);
	        	mProgressBar2.setVisibility(View.INVISIBLE);
			}
		};

		//Fetching images in separate threads
		new Thread()
		{
			public void run()
			{
				drawable1 = ConnectionManager.getConnectionManager().getImage("http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data");
				runOnUiThread(setImageThread1);
			}
		}.start();
		new Thread()
		{
			public void run()
			{
				drawable2 = ConnectionManager.getConnectionManager().getImage("http://apps.dhis2.org/demo/api/charts/MHKr9RGieUL/data");
		        runOnUiThread(setImageThread2);
			}
		}.start();	
		
	}
}
