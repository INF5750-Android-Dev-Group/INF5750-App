package no.uio.inf5750.assignment3.dashboard;

import no.uio.inf5750.assignment3.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class DashboardChartActivity extends Activity {

	private ProgressBar mProgressBar;	
	private ImageView mImageView;
	private Button mButtonShare, mButtonInsert, mButtonClear;
	private Bundle mExtras;

	private Bitmap mBmp;
	// Working variables for the images' OnTouchListener:
	private Matrix mMtx = new Matrix(), mTempMtx = new Matrix();
	private PointF mContact = new PointF(), mMidpoint = new PointF();
	private float mDist, mTempDist;
	private static final int PASSIVE = 0, DRAGGING = 1, ZOOMING = 2;
	private int mState = PASSIVE;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboardchart);

		mImageView = (ImageView) findViewById(R.id.dashboardchart_imageview);
		mProgressBar = (ProgressBar) findViewById(R.id.dashboardchart_progress);
		mButtonShare = (Button) findViewById(R.id.dashboardchart_btnShare);
		mButtonInsert = (Button) findViewById(R.id.dashboardchart_btnInsert);
		mButtonClear = (Button) findViewById(R.id.dashboardchart_btnClear);

		setButtons();
		setImage();
	}

	private void setImage()
	{ 
		final Thread setImageThread = new Thread(){
			public void run()
			{ //Separate thread to be run on UI
				if(mBmp != null)
				{
		        	mImageView.setImageBitmap(mBmp);
				}
				else
				{
					mImageView.setImageDrawable(getResources().getDrawable(R.drawable.notfound));
				}
				mImageView.setVisibility(View.VISIBLE);
	        	mProgressBar.setVisibility(View.INVISIBLE);
			}
		};

		//Fetching images in separate threads
		new Thread()
		{
			public void run()
			{
				mExtras = getIntent().getExtras();
				try {
					byte [] barr = mExtras.getByteArray("dashboard_chart");
					mBmp = BitmapFactory.decodeByteArray(barr, 0, barr.length);
				} catch (NullPointerException npe) {
					npe.printStackTrace();
					mBmp = null;
				}
				runOnUiThread(setImageThread);
			}
		}.start();
		
		mImageView.setOnTouchListener(new OnTouchListener()
		{
			/** Called when touch gestures are registered on the chart.
			 * Handles panning and zooming. */
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView view = (ImageView) v;
				
				switch(event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					mTempMtx.set(mMtx);
					mContact.set(event.getX(), event.getY());
					mState = DRAGGING;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mState = PASSIVE;
					break;
				case MotionEvent.ACTION_MOVE:
					if (mState == DRAGGING) {
						mMtx.set(mTempMtx);
						mMtx.postTranslate(event.getX() - mContact.x, event.getY() - mContact.y);
					}
					else if (mState == ZOOMING) {
						mDist = getDist(event);
						if (mDist > 10f) {
							mMtx.set(mTempMtx);
							float scale = mDist / mTempDist;
							mMtx.postScale(scale, scale, mMidpoint.x, mMidpoint.y);
						}
					}
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					mTempDist = getDist(event);
					if (mTempDist > 10f) {
						mTempMtx.set(mMtx);
						setMid(mMidpoint, event);
						mState = ZOOMING;
					}
					break;
				}
				
				view.setImageMatrix(mMtx);
				return true;
			}
			
			private float getDist(MotionEvent event) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x*x + y*y);
			}
			
			private void setMid(PointF p, MotionEvent event) {
				float x = event.getX(0) + event.getX(1);
				float y = event.getY(0) + event.getY(1);
				p.set(x/2, y/2);
			}
		});
	}
	
	private void setButtons() {
		mButtonShare.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				Toast toast = Toast.makeText(DashboardChartActivity.this,
						R.string.general_notimplemented, Toast.LENGTH_LONG);
				toast.show();
			}
		});

		mButtonInsert.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				Toast toast = Toast.makeText(DashboardChartActivity.this,
						R.string.general_notimplemented, Toast.LENGTH_LONG);
				toast.show();
			}
		});
		
		mButtonClear.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				Toast toast = Toast.makeText(DashboardChartActivity.this,
						R.string.general_notimplemented, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}
}
