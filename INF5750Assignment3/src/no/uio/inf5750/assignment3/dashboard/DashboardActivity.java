package no.uio.inf5750.assignment3.dashboard;

import java.io.ByteArrayOutputStream;
import java.util.TreeMap;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.util.ConnectionManager;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;

public class DashboardActivity extends Activity {
	private ProgressBar mProgressBar1, mProgressBar2;
	
	private ImageView mImageView1, mImageView2;
	private Button mButtonPrevPage, mButtonNextPage;

	private Drawable mDrawable1;
	private Drawable mDrawable2;
	
	// TEMPORARY SOLUTION UNTIL USER SETTINGS ARE RETRIEVABLE:
	private TreeMap<String, String> mCharts = new TreeMap<String, String>();
	private String mChart1 = "http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data";
	private String mChart2 = "http://apps.dhis2.org/demo/api/charts/MHKr9RGieUL/data";
	private int mChartToReplace = 0;
	private boolean mInContextMenu = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Contacts server to get currently available charts
		UpdateDaemon.getDaemon().updateCharts();
		
		updateChartsMap(); // TEMPORARY SOLUTION

		mButtonPrevPage = (Button) findViewById(R.id.dashboard_btnPrevPage);
		mButtonNextPage = (Button) findViewById(R.id.dashboard_btnNextPage);
		mImageView1 = (ImageView) findViewById(R.id.main_imageview1);
		mImageView2 = (ImageView) findViewById(R.id.main_imageview2);
		mProgressBar1 = (ProgressBar) findViewById(R.id.diagram_progress1);
		mProgressBar2 = (ProgressBar) findViewById(R.id.diagram_progress2);

		registerForContextMenu(mImageView1);
		registerForContextMenu(mImageView2);
		
		setButtons();
		setImages();
	}
	
	/** Opens context menu when long-pressing a chart. */
	@Override
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		mInContextMenu = true;
		super.onCreateContextMenu(menu, v, menuInfo);

		// Contacts server to get currently available charts
		UpdateDaemon.getDaemon().updateCharts();
		updateChartsMap();
		
		if (mCharts.isEmpty()) {
			Toast toast = Toast.makeText(DashboardActivity.this,
					"Error: No charts available.", Toast.LENGTH_LONG);
			toast.show();
			mChartToReplace = 0;
			mInContextMenu = false;
			return;
		}

		// Determine which chart triggered the context menu:
		if (((ImageView) v).getId() == R.id.main_imageview1) {
			mChartToReplace = 1;
		} else if (((ImageView) v).getId() == R.id.main_imageview2) {
			mChartToReplace = 2;
		} else { // Long-press outside of a chart.
			mChartToReplace = 0;
			mInContextMenu = false;
			return;
		}
		
		menu.setHeaderTitle("Replace with...");
		
		// Populate menu with available charts:
		for (String label : mCharts.keySet()) {
			menu.add(0, v.getId(), 0, label);
		}
	}
	
	/** Called when an element is selected from context menu.
	 * @return Returns true when chart is successfully replaced,
	 *         otherwise false. */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Get URL for chart user selected:
		String tempURL = mCharts.get(item.getTitle());
		
		// Return if URL not found:
		if (tempURL == null) {
			mInContextMenu = false;
			return false;
		}
		
		// Replace chart to display with newly selected chart:
		if (mChartToReplace == 1) {
			mChart1 = tempURL;
		} else if (mChartToReplace == 2) {
			mChart2 = tempURL;
		} else {
			mInContextMenu = false;
			return false;
		}
		setImages();
		mInContextMenu = false;
		return true;
	}

	// TEMPORARY SOLUTION UNTIL USER SETTINGS ARE RETRIEVABLE:
	public void updateChartsMap() {
		UpdateDaemon.getDaemon().repopulateSortedChartImageHrefTree(mCharts);
		/*mCharts.put("ANC Coverages", "http://apps.dhis2.org/demo/api/charts/R0DVGvXDUNP/data");
		mCharts.put("ANC: 1-3 Coverage", "http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data");
		mCharts.put("Delivery: Births attended by skilled health personel", "http://apps.dhis2.org/demo/api/charts/E9D9KmjyHnd/data");
		mCharts.put("Disease: Mortality", "http://apps.dhis2.org/demo/api/charts/xuNxD7c6pmM/data");
		mCharts.put("HIV: ZDV rates April to July 2012", "http://apps.dhis2.org/demo/api/charts/PuvO0syzFoz/data");
		mCharts.put("Immunization Indicators", "http://apps.dhis2.org/demo/api/charts/nxopkCcmdcT/data");
		mCharts.put("Immunization: All Vaccines Yearly", "http://apps.dhis2.org/demo/api/charts/INtKDA1VJC0/data");
		mCharts.put("Immunization: Drop Out Penta 1-3", "http://apps.dhis2.org/demo/api/charts/hjrucWPaRG4/data");
		mCharts.put("Immunization: Essential vaccines", "http://apps.dhis2.org/demo/api/charts/pRBQ77mhEJ8/data");
		mCharts.put("Immunization: Fully Immunized by Facility Type Yearly", "http://apps.dhis2.org/demo/api/charts/fxIAgTaTFwJ/data");
		mCharts.put("Morbidity: New cases by diagnosis Pie", "http://apps.dhis2.org/demo/api/charts/bML2tPOWjUE/data");
		mCharts.put("Nutrition: Malnutrition and well nourished", "http://apps.dhis2.org/demo/api/charts/CVE8AxBsJFc/data");
		mCharts.put("RCH: ANC-Delivery-Immunisation Yearly", "http://apps.dhis2.org/demo/api/charts/MHKr9RGieUL/data");
		mCharts.put("RCH: ANC-Delivery-Reporting Rates Monthly", "http://apps.dhis2.org/demo/api/charts/gYhEwyaZTSO/data");*/
	}
	
	/** Method for initializing button listeners. */
	private void setButtons() {
		// "Previous page" button
		mButtonPrevPage.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				Toast toast = Toast.makeText(DashboardActivity.this,
						R.string.general_notimplemented, Toast.LENGTH_LONG);
				toast.show();
			}
		});

		// "Next page" button
		mButtonNextPage.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				Toast toast = Toast.makeText(DashboardActivity.this,
						R.string.general_notimplemented, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}
	
	/** Method for initializing chart images and their listeners. */
	private void setImages()
	{ //fetching data in a thread for making things appear smoother

		final Thread setImageThread1 = new Thread(){
			public void run()
			{
				if(mDrawable1!=null)
				{
		        	mImageView1.setImageDrawable(mDrawable1);
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
			{
		        if(mDrawable2!=null)
		        {
		        	mImageView2.setImageDrawable(mDrawable2);
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
				mDrawable1 = ConnectionManager.getConnectionManager().getImage(mChart1);
				mImageView1.setOnTouchListener(new DashboardOnTouchListener(mDrawable1));
				runOnUiThread(setImageThread1);
			}
		}.start();

		new Thread()
		{
			public void run()
			{
				mDrawable2 = ConnectionManager.getConnectionManager().getImage(mChart2);
				mImageView2.setOnTouchListener(new DashboardOnTouchListener(mDrawable2));
		        runOnUiThread(setImageThread2);
			}
		}.start();
	}
	

	public void launchChartActivity(Drawable image)
	{
		// Convert to byte array for sending through Extras.
		Bitmap bmp = ((BitmapDrawable) image).getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, bos); // NOTE: THIS ASSUMES PNGs!
		byte[] imageBytes = bos.toByteArray();

		Intent intent = new Intent(this, DashboardChartActivity.class);
		intent.putExtra("dashboard_chart", imageBytes);
		startActivity(intent);
	}
	
	/** Touch listener class for the chart images. */
	class DashboardOnTouchListener implements OnTouchListener {
		Drawable drawable;
		
		DashboardOnTouchListener(Drawable draw) {
			drawable = draw;
		}
		
		/** Called when a touch is registered on the charts.
		 * Only registers short-presses to avoid interfering with long-press context menu. */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP && !mInContextMenu) {
				launchChartActivity(drawable);
			}
			
			return false;
		}
	}
}
