package no.uio.inf5750.assignment3.dashboard;

import java.io.ByteArrayOutputStream;
import java.util.TreeMap;

import no.uio.inf5750.assignment3.R;
import no.uio.inf5750.assignment3.util.ConnectionManager;
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
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;

public class DashboardActivity extends Activity {

	//private ProgressDialog mProgressDialog;
	private ProgressBar mProgressBar1, mProgressBar2;
	
	private ImageView mImageView1, mImageView2;
	private Button mButtonPrevPage, mButtonNextPage;
	
	// TEMPORARY, FOR TESTING:
	private TreeMap<String, String> charts = new TreeMap<String, String>();
	private String chart1 = "http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data";
	private String chart2 = "http://apps.dhis2.org/demo/api/charts/MHKr9RGieUL/data";
	private int chartToReplace = 0;
	private boolean inContextMenu = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initialiseChartsMap(); // TEMPORARY, FOR TESTING

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
	
	@Override
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		inContextMenu = true;
		super.onCreateContextMenu(menu,  v,  menuInfo);
		if (((ImageView) v).getId() == R.id.main_imageview1) {
			chartToReplace = 1;
		} else if (((ImageView) v).getId() == R.id.main_imageview2) {
			chartToReplace = 2;
		} else {
			chartToReplace = 0;
			inContextMenu = false;
			return;
		}
		menu.setHeaderTitle("Replace with...");
		for (String label : charts.keySet()) {
			menu.add(0, v.getId(), 0, label);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String tempURL = charts.get(item.getTitle());
		if (tempURL == null) {
			inContextMenu = false;
			return false;
		}
		if (chartToReplace == 1) {
			chart1 = tempURL;
		} else if (chartToReplace == 2) {
			chart2 = tempURL;
		} else {
			inContextMenu = false;
			return false;
		}
		setImages();
		inContextMenu = false;
		return true;
	}
	
	// TEMPORARY, FOR TESTING:
	public void initialiseChartsMap() {
		charts.put("ANC Coverages", "http://apps.dhis2.org/demo/api/charts/R0DVGvXDUNP/data");
		charts.put("ANC: 1-3 Coverage", "http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data");
		charts.put("Delivery: Births attended by skilled health personel", "http://apps.dhis2.org/demo/api/charts/E9D9KmjyHnd/data");
		charts.put("Disease: Mortality", "http://apps.dhis2.org/demo/api/charts/xuNxD7c6pmM/data");
		charts.put("HIV: ZDV rates April to July 2012", "http://apps.dhis2.org/demo/api/charts/PuvO0syzFoz/data");
		charts.put("Immunization Indicators", "http://apps.dhis2.org/demo/api/charts/nxopkCcmdcT/data");
		charts.put("Immunization: All Vaccines Yearly", "http://apps.dhis2.org/demo/api/charts/INtKDA1VJC0/data");
		charts.put("Immunization: Drop Out Penta 1-3", "http://apps.dhis2.org/demo/api/charts/hjrucWPaRG4/data");
		charts.put("Immunization: Essential vaccines", "http://apps.dhis2.org/demo/api/charts/pRBQ77mhEJ8/data");
		charts.put("Immunization: Fully Immunized by Facility Type Yearly", "http://apps.dhis2.org/demo/api/charts/fxIAgTaTFwJ/data");
		charts.put("Morbidity: New cases by diagnosis Pie", "http://apps.dhis2.org/demo/api/charts/bML2tPOWjUE/data");
		charts.put("Nutrition: Malnutrition and well nourished", "http://apps.dhis2.org/demo/api/charts/CVE8AxBsJFc/data");
		charts.put("RCH: ANC-Delivery-Immunisation Yearly", "http://apps.dhis2.org/demo/api/charts/MHKr9RGieUL/data");
		charts.put("RCH: ANC-Delivery-Reporting Rates Monthly", "http://apps.dhis2.org/demo/api/charts/gYhEwyaZTSO/data");
	}
	
	private void setButtons() {
		mButtonPrevPage.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
			}
		});

		mButtonNextPage.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
			}
		});
	}
	
	Drawable drawable1;
	Drawable drawable2;
	private void setImages()
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
//				drawable1 = ConnectionManager.getConnectionManager().getImage("http://apps.dhis2.org/demo/api/charts/EbRN2VIbPdV/data");
				drawable1 = ConnectionManager.getConnectionManager().getImage(chart1);
				mImageView1.setOnTouchListener(new DashboardOnTouchListener(drawable1));
				runOnUiThread(setImageThread1);
			}
		}.start();

		new Thread()
		{
			public void run()
			{
//				drawable2 = ConnectionManager.getConnectionManager().getImage("http://apps.dhis2.org/demo/api/charts/MHKr9RGieUL/data");
				drawable2 = ConnectionManager.getConnectionManager().getImage(chart2);
				mImageView2.setOnTouchListener(new DashboardOnTouchListener(drawable2));
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
	
	class DashboardOnTouchListener implements OnTouchListener {
		Drawable drawable;
		
		DashboardOnTouchListener(Drawable draw) {
			drawable = draw;
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP && !inContextMenu) {
				launchChartActivity(drawable);
			}
			
			return false;
		}
	}
}
